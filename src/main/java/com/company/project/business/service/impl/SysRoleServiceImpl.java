package com.company.project.business.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.business.enums.DeletedEnum;
import com.company.project.business.service.*;
import com.company.project.business.vo.permission.PermissionRespNode;
import com.company.project.business.vo.role.RoleAddReqVO;
import com.company.project.business.vo.role.RolePageReqVO;
import com.company.project.business.vo.role.RolePermissionOperationReqVO;
import com.company.project.business.vo.role.RoleUpdateReqVO;
import com.company.project.framework.exception.BusinessException;
import com.company.project.framework.exception.code.BaseResponseCode;
import com.company.project.framework.object.PageResult;
import com.company.project.framework.object.ServiceImpl;
import com.company.project.persistence.beans.SysRole;
import com.company.project.persistence.mapper.SysRoleMapper;
import com.company.project.persistence.mapper.SysUserRoleMapper;
import com.company.project.util.BeanConvertUtil;
import com.company.project.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
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
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private ISysUserRoleService userRoleService;
    @Autowired
    private ISysRolePermissionService rolePermissionService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private ISysPermissionService permissionService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysRole addRole(RoleAddReqVO vo) {

        SysRole sysRole = BeanConvertUtil.doConvert(vo, SysRole.class);
        sysRole.setCreateTime(LocalDateTime.now());
        int count = sysRoleMapper.insert(sysRole);
        if (count != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        if (null != vo.getPermissions() && !vo.getPermissions().isEmpty()) {
            RolePermissionOperationReqVO reqVO = new RolePermissionOperationReqVO();
            reqVO.setRoleId(sysRole.getId());
            reqVO.setPermissionIds(vo.getPermissions());
            rolePermissionService.addRolePermission(reqVO);
        }

        return sysRole;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateRole(RoleUpdateReqVO vo) {
        SysRole sysRole = sysRoleMapper.selectById(vo.getId());
        if (null == sysRole) {
            log.error("传入 的 id:{}不合法", vo.getId());
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        SysRole update = BeanConvertUtil.doConvert(vo, SysRole.class);
        update.setUpdateTime(LocalDateTime.now());
        int count = sysRoleMapper.updateById(update);
        if (count != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        rolePermissionService.removeByRoleId(sysRole.getId());
        if (null != vo.getPermissions() && !vo.getPermissions().isEmpty()) {
            RolePermissionOperationReqVO reqVO = new RolePermissionOperationReqVO();
            reqVO.setRoleId(sysRole.getId());
            reqVO.setPermissionIds(vo.getPermissions());
            rolePermissionService.addRolePermission(reqVO);

            List<Long> userIds = sysUserRoleMapper.getInfoByUserIdByRoleId(vo.getId());
        }

    }

    @Override
    public SysRole detailInfo(Long id) {
        SysRole sysRole = sysRoleMapper.selectById(id);
        if (sysRole == null) {
            log.error("传入 的 id:{}不合法", id);
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        List<PermissionRespNode> permissionRespNodes = permissionService.selectAllByTree();
        Set<Long> checkList = new HashSet<>(rolePermissionService.getPermissionIdsByRoleId(sysRole.getId()));
        setheckced(permissionRespNodes, checkList);
        sysRole.setPermissionRespNodes(permissionRespNodes);
        return sysRole;
    }


    private void setheckced(List<PermissionRespNode> list, Set<Long> checkList) {

        for (PermissionRespNode node : list) {

            if (checkList.contains(node.getId()) && (node.getChildren() == null || node.getChildren().isEmpty())) {
                node.setChecked(true);
            }
            setheckced((List<PermissionRespNode>) node.getChildren(), checkList);

        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deletedRole(Long id) {
        SysRole sysRole = new SysRole();
        sysRole.setId(id);
        sysRole.setUpdateTime(LocalDateTime.now());
        sysRole.setDeleted(DeletedEnum.deleted);
        int count = sysRoleMapper.updateById(sysRole);
        if (count != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        List<Long> userIds = sysUserRoleMapper.getInfoByUserIdByRoleId(id);
        rolePermissionService.removeByRoleId(id);
        userRoleService.removeByRoleId(id);

    }

    @Override
    public PageResult<SysRole> pageInfo(RolePageReqVO vo) {
        Page<SysRole> page = new Page<>(vo.getPageNumber(), vo.getPageSize());

        SysRole sysRole = BeanConvertUtil.doConvert(vo, SysRole.class);

        Page<SysRole> rolePage = sysRoleMapper.selectPage(page, Wrappers.query(sysRole));

        return ResultUtil.tablePage(rolePage);
    }

    @Override
    public List<SysRole> getRoleInfoByUserId(Long userId) {

        List<Long> roleIds = userRoleService.getRoleIdsByUserId(userId);
        if (roleIds.isEmpty()) {
            return null;
        }
        return sysRoleMapper.selectBatchIds(roleIds);
    }

    @Override
    public List<String> getRoleNames(Long userId) {

        List<SysRole> sysRoles = getRoleInfoByUserId(userId);
        if (null == sysRoles || sysRoles.isEmpty()) {
            return null;
        }
        List<String> list = new ArrayList<>();
        for (SysRole sysRole : sysRoles) {
            list.add(sysRole.getName());
        }
        return list;
    }

    @Override
    public List<SysRole> selectAllRoles() {

        return sysRoleMapper.selectList(Wrappers.query());
    }
}
