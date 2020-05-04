package com.company.project.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.business.consts.ProjectConstant;
import com.company.project.business.enums.DeletedEnum;
import com.company.project.business.service.*;
import com.company.project.business.vo.user.*;
import com.company.project.framework.exception.BusinessException;
import com.company.project.framework.exception.code.BaseResponseCode;
import com.company.project.framework.object.PageResult;
import com.company.project.framework.object.ServiceImpl;
import com.company.project.persistence.beans.SysRole;
import com.company.project.persistence.beans.SysUser;
import com.company.project.persistence.mapper.SysUserMapper;
import com.company.project.util.BeanConvertUtil;
import com.company.project.util.Md5Util;
import com.company.project.util.PasswordUtil;
import com.company.project.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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

    @Override
    public Long register(RegisterReqVO vo) {
        SysUser sysUser = BeanConvertUtil.doConvert(vo, SysUser.class);
        sysUser.setSalt(Md5Util.MD5(vo.getUsername()+ ProjectConstant.APP_SECURITY_KEY));
        String encode = null;
        try {
            encode = PasswordUtil.encrypt(vo.getPassword(), sysUser.getSalt());
        } catch (Exception e) {
            e.printStackTrace();
        }
        sysUser.setPassword(encode);
        sysUser.setCreateTime(LocalDateTime.now());
        int i = sysUserMapper.insert(sysUser);
        if (i != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        return sysUser.getId();
    }

    private List<String> getRolesByUserId(Long userId) {
        return roleService.getRoleNames(userId);
    }

    private Set<String> getPermissionsByUserId(Long userId) {
        return permissionService.getPermissionsByUserId(userId);
    }


    @Override
    public void updateUserInfo(UserUpdateReqVO vo, Long operationId) {

        SysUser sysUser = sysUserMapper.selectById(vo.getId());
        if (null == sysUser) {
            log.error("传入 的 id:{}不合法", vo.getId());
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        BeanUtils.copyProperties(vo, sysUser);
        sysUser.setUpdateTime(LocalDateTime.now());
        if (!StringUtils.isEmpty(vo.getPassword())) {
            String newPassword = null;
            try {
                newPassword = PasswordUtil.encrypt(vo.getPassword(), sysUser.getSalt());
            } catch (Exception e) {
                e.printStackTrace();
            }
            sysUser.setPassword(newPassword);
        } else {
            sysUser.setPassword(null);
        }
        sysUser.setUpdateId(operationId.toString());
        int count = sysUserMapper.updateById(sysUser);
        if (count != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        setUserOwnRole(sysUser.getId(), vo.getRoleIds());

    }

    @Override
    public PageResult<SysUser> pageInfo(UserPageReqVO vo) {

        Page<SysUser> page = new Page<>(vo.getPageNumber(), vo.getPageSize());

        SysUser sysUser = BeanConvertUtil.doConvert(vo, SysUser.class);
        Page<SysUser> userPage = sysUserMapper.selectPage(page, Wrappers.query(sysUser));


        return ResultUtil.tablePage(userPage);
    }

    @Override
    public SysUser detailInfo(Long userId) {

        return sysUserMapper.selectById(userId);
    }


    @Override
    public void addUser(UserAddReqVO vo) {
        SysUser sysUser = BeanConvertUtil.doConvert(vo, SysUser.class);
        sysUser.setSalt(Md5Util.MD5(sysUser.getUsername()+ ProjectConstant.APP_SECURITY_KEY));
        String encode = null;
        try {
            encode = PasswordUtil.encrypt(vo.getPassword(), sysUser.getSalt());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public void updatePwd(UpdatePasswordReqVO vo, Long userId) {

        SysUser sysUser = sysUserMapper.selectById(userId);
        if (sysUser == null) {
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        try {
            if (!PasswordUtil.matches(sysUser.getSalt(), vo.getOldPwd(), sysUser.getPassword())) {
                throw new BusinessException(BaseResponseCode.OLD_PASSWORD_ERROR);
            }
        } catch (Exception e) {
            throw new BusinessException(BaseResponseCode.OLD_PASSWORD_ERROR);
        }
        sysUser.setUpdateTime(LocalDateTime.now());
        try {
            sysUser.setPassword(PasswordUtil.encrypt(vo.getNewPwd(), sysUser.getSalt()));
        } catch (Exception e) {
            throw new BusinessException(BaseResponseCode.OLD_PASSWORD_ERROR);
        }
        int i = sysUserMapper.updateById(sysUser);
        if (i != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
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
        return sysUserMapper.selectUserRoles(appId);
    }


}
