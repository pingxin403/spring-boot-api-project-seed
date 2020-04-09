package com.company.project.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.business.consts.JwtConstant;
import com.company.project.business.enums.DeletedEnum;
import com.company.project.business.enums.StatusEnum;
import com.company.project.business.service.*;
import com.company.project.business.vo.user.*;
import com.company.project.framework.exception.BusinessException;
import com.company.project.framework.exception.code.BaseResponseCode;
import com.company.project.framework.object.PageResult;
import com.company.project.framework.object.ServiceImpl;
import com.company.project.framework.property.JwtProperties;
import com.company.project.persistence.beans.SysRole;
import com.company.project.persistence.beans.SysUser;
import com.company.project.persistence.mapper.SysUserMapper;
import com.company.project.util.JwtTokenUtil;
import com.company.project.util.PasswordUtil;
import com.company.project.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author pingxin
 * @since 2020-03-03
 */
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ISysRoleService roleService;
    @Autowired
    private ISysPermissionService permissionService;
    @Autowired
    private ISysUserRoleService userRoleService;
    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public Long register(RegisterReqVO vo) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(vo, sysUser);
        sysUser.setSalt(PasswordUtil.getSalt());
        String encode = PasswordUtil.encode(vo.getPassword(), sysUser.getSalt());
        sysUser.setPassword(encode);
        sysUser.setCreateTime(LocalDateTime.now());
        int i = sysUserMapper.insert(sysUser);
        if (i != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        return sysUser.getId();
    }

    @Override
    public LoginRespVO login(LoginReqVO vo) {
        SysUser sysUser = sysUserMapper.getUserInfoByName(vo.getUsername());
        if (null == sysUser) {
            throw new BusinessException(BaseResponseCode.NOT_ACCOUNT);
        }
        if (sysUser.getStatus() == StatusEnum.LOCKED) {
            throw new BusinessException(BaseResponseCode.USER_LOCK);
        }
        if (!PasswordUtil.matches(sysUser.getSalt(), vo.getPassword(), sysUser.getPassword())) {
            throw new BusinessException(BaseResponseCode.PASSWORD_ERROR);
        }
        LoginRespVO respVO = new LoginRespVO();
        BeanUtils.copyProperties(sysUser, respVO);
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtConstant.JWT_PERMISSIONS_KEY, getPermissionsByUserId(sysUser.getId()));
        claims.put(JwtConstant.JWT_ROLES_KEY, getRolesByUserId(sysUser.getId()));
        claims.put(JwtConstant.JWT_USER_NAME, sysUser.getUsername());
        String access_token = JwtTokenUtil.getAccessToken(sysUser.getId().toString(), claims);
        String refresh_token;
        if ("1".equals(vo.getType())) {
            refresh_token = JwtTokenUtil.getRefreshToken(sysUser.getId().toString(), claims);
        } else {
            refresh_token = JwtTokenUtil.getRefreshAppToken(sysUser.getId().toString(), claims);
        }
        respVO.setAccessToken(access_token);
        respVO.setRefreshToken(refresh_token);
        return respVO;
    }

    private List<String> getRolesByUserId(Long userId) {
        return roleService.getRoleNames(userId);
    }

    private Set<String> getPermissionsByUserId(Long userId) {
        return permissionService.getPermissionsByUserId(userId);
    }

    @Override
    public String refreshToken(String refreshToken, String accessToken) {

        if (redisService.hasKey(JwtConstant.JWT_ACCESS_TOKEN_BLACKLIST + refreshToken) || !JwtTokenUtil.validateToken(refreshToken)) {
            throw new BusinessException(BaseResponseCode.TOKEN_ERROR);
        }
        Long userId = Long.parseLong(JwtTokenUtil.getUserId(refreshToken));
        log.info("userId={}", userId);
        SysUser sysUser = sysUserMapper.selectById(userId);
        if (null == sysUser) {
            throw new BusinessException(BaseResponseCode.TOKEN_PARSE_ERROR);
        }
        Map<String, Object> claims = null;

        if (redisService.hasKey(JwtConstant.JWT_REFRESH_KEY + userId)) {
            claims = new HashMap<>();
            claims.put(JwtConstant.JWT_ROLES_KEY, getRolesByUserId(userId));
            claims.put(JwtConstant.JWT_PERMISSIONS_KEY, getPermissionsByUserId(userId));
        }
        String newAccessToken = JwtTokenUtil.refreshToken(refreshToken, claims);

        redisService.set(JwtConstant.JWT_REFRESH_STATUS + accessToken, userId, 1, TimeUnit.MINUTES);


        if (redisService.hasKey(JwtConstant.JWT_REFRESH_KEY + userId)) {
            redisService.set(JwtConstant.JWT_REFRESH_IDENTIFICATION + newAccessToken, userId, redisService.getExpire(JwtConstant.JWT_REFRESH_KEY + userId), TimeUnit.MILLISECONDS);
        }
        return newAccessToken;
    }

    @Override
    public void updateUserInfo(UserUpdateReqVO vo, String operationId) {

        SysUser sysUser = sysUserMapper.selectById(vo.getId());
        if (null == sysUser) {
            log.error("传入 的 id:{}不合法", vo.getId());
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        BeanUtils.copyProperties(vo, sysUser);
        sysUser.setUpdateTime(LocalDateTime.now());
        if (!StringUtils.isEmpty(vo.getPassword())) {
            String newPassword = PasswordUtil.encode(vo.getPassword(), sysUser.getSalt());
            sysUser.setPassword(newPassword);
        } else {
            sysUser.setPassword(null);
        }
        sysUser.setUpdateId(operationId);
        int count = sysUserMapper.updateById(sysUser);
        if (count != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        setUserOwnRole(sysUser.getId(), vo.getRoleIds());
        if (vo.getStatus().equals(StatusEnum.LOCKED)) {
            redisService.set(JwtConstant.ACCOUNT_LOCK_KEY + sysUser.getId(), sysUser.getId());
        } else {
            redisService.del(JwtConstant.ACCOUNT_LOCK_KEY + sysUser.getId());
        }

    }

    @Override
    public PageResult<SysUser> pageInfo(UserPageReqVO vo) {

        Page<SysUser> page = new Page<>(vo.getPageNumber(), vo.getPageSize());

        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(vo, sysUser);
        Page<SysUser> userPage = sysUserMapper.selectPage(page, Wrappers.query(sysUser));


        return ResultUtil.tablePage(userPage);
    }

    @Override
    public SysUser detailInfo(Long userId) {

        return sysUserMapper.selectById(userId);
    }



    @Override
    public void addUser(UserAddReqVO vo) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(vo, sysUser);
        sysUser.setSalt(PasswordUtil.getSalt());
        String encode = PasswordUtil.encode(vo.getPassword(), sysUser.getSalt());
        sysUser.setPassword(encode);
        sysUser.setCreateTime(LocalDateTime.now());
        int i = sysUserMapper.insert(sysUser);
        if (i != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        if (null != vo.getRoleIds() && !vo.getRoleIds().isEmpty()) {
            UserRoleOperationReqVO reqVO = new UserRoleOperationReqVO();
            reqVO.setUserId(sysUser.getId());
            reqVO.setRoleIds(vo.getRoleIds());
            userRoleService.addUserRoleInfo(reqVO);
        }
    }

    @Override
    public void logout(String accessToken, String refreshToken) {
        if (StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(refreshToken)) {
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        Subject subject = SecurityUtils.getSubject();
        log.info("subject.getPrincipals()={}", subject.getPrincipals());
        if (subject.isAuthenticated()) {
            subject.logout();
        }
        String userId = JwtTokenUtil.getUserId(accessToken);

        redisService.set(JwtConstant.JWT_REFRESH_TOKEN_BLACKLIST + accessToken, userId, JwtTokenUtil.getRemainingTime(accessToken), TimeUnit.MILLISECONDS);

        redisService.set(JwtConstant.JWT_REFRESH_TOKEN_BLACKLIST + refreshToken, userId, JwtTokenUtil.getRemainingTime(refreshToken), TimeUnit.MILLISECONDS);


        redisService.del(JwtConstant.IDENTIFY_CACHE_KEY + userId);
    }

    @Override
    public void updatePwd(UpdatePasswordReqVO vo, Long userId, String accessToken, String refreshToken) {

        SysUser sysUser = sysUserMapper.selectById(userId);
        if (sysUser == null) {
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        if (!PasswordUtil.matches(sysUser.getSalt(), vo.getOldPwd(), sysUser.getPassword())) {
            throw new BusinessException(BaseResponseCode.OLD_PASSWORD_ERROR);
        }
        sysUser.setUpdateTime(LocalDateTime.now());
        sysUser.setPassword(PasswordUtil.encode(vo.getNewPwd(), sysUser.getSalt()));
        int i = sysUserMapper.updateById(sysUser);
        if (i != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }

        redisService.set(JwtConstant.JWT_REFRESH_TOKEN_BLACKLIST + accessToken, userId, JwtTokenUtil.getRemainingTime(accessToken), TimeUnit.MILLISECONDS);

        redisService.set(JwtConstant.JWT_REFRESH_TOKEN_BLACKLIST + refreshToken, userId, JwtTokenUtil.getRemainingTime(refreshToken), TimeUnit.MILLISECONDS);


        redisService.del(JwtConstant.IDENTIFY_CACHE_KEY + userId);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletedUsers(List<Long> userIds, String operationId) {
        SysUser sysUser = new SysUser();
        sysUser.setUpdateId(operationId);
        sysUser.setUpdateTime(LocalDateTime.now());
        sysUser.setDeleted(DeletedEnum.deleted);
        int i = sysUserMapper.deletedUsers(sysUser, userIds);
        if (i == 0) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }

        for (Long userId : userIds) {
            redisService.set(JwtConstant.DELETED_USER_KEY + userId, userId, jwtProperties.getRefreshTokenExpireAppTime().toMillis(), TimeUnit.MILLISECONDS);
            //清空权鉴缓存
            redisService.del(JwtConstant.IDENTIFY_CACHE_KEY + userId);
        }

    }

    @Override
    public UserOwnRoleRespVO getUserOwnRole(Long userId) {
        List<Long> roleIdsByUserId = userRoleService.getRoleIdsByUserId(userId);
        List<SysRole> list = roleService.selectAllRoles();
        UserOwnRoleRespVO vo = new UserOwnRoleRespVO();
        vo.setAllRole(list);
        vo.setOwnRoles(roleIdsByUserId);
        return vo;
    }

    @Override
    public void setUserOwnRole(Long userId, List<Long> roleIds) {
        userRoleService.removeByUserId(userId);
        if (null != roleIds && !roleIds.isEmpty()) {

            UserRoleOperationReqVO reqVO = new UserRoleOperationReqVO();
            reqVO.setUserId(userId);
            reqVO.setRoleIds(roleIds);
            userRoleService.addUserRoleInfo(reqVO);
        }


        redisService.set(JwtConstant.JWT_REFRESH_KEY + userId, userId, jwtProperties.getAccessTokenExpireTime().toMillis(), TimeUnit.MILLISECONDS);
        //清空权鉴缓存
        redisService.del(JwtConstant.IDENTIFY_CACHE_KEY + userId);
    }

    @Override
    public SysUser getByUserName(String username) {
        QueryWrapper<SysUser> wrapper = Wrappers.query();
        wrapper.eq("username", username);
        SysUser sysUser = sysUserMapper.selectOne(wrapper);
        return sysUser;
    }

    @Override
    public String loadAccountRole(Long appId) {
       return   sysUserMapper.selectUserRoles(appId);
    }


}
