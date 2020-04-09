package com.company.project.business.service.impl;


import com.company.project.business.service.ISysRolePermissionService;
import com.company.project.business.vo.role.RolePermissionOperationReqVO;
import com.company.project.framework.exception.BusinessException;
import com.company.project.framework.exception.code.BaseResponseCode;
import com.company.project.framework.object.ServiceImpl;
import com.company.project.persistence.beans.SysRolePermission;
import com.company.project.persistence.mapper.SysRolePermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author pingxin
 * @since 2020-03-03
 */
@Service
public class SysRolePermissionServiceImpl extends ServiceImpl<SysRolePermissionMapper, SysRolePermission> implements ISysRolePermissionService {
    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Override
    public int removeByRoleId(Long roleId) {
        return sysRolePermissionMapper.removeByRoleId(roleId);
    }

    @Override
    public List<Long> getPermissionIdsByRoles(List<Long> roleIds) {
        return sysRolePermissionMapper.getPermissionIdsByRoles(roleIds);
    }

    @Override
    public void addRolePermission(RolePermissionOperationReqVO vo) {

        List<SysRolePermission> list = new ArrayList<>();
        for (Long permissionId : vo.getPermissionIds()) {
            SysRolePermission sysRolePermission = new SysRolePermission();
            sysRolePermission.setCreateTime(LocalDateTime.now());
            sysRolePermission.setPermissionId(permissionId);
            sysRolePermission.setRoleId(vo.getRoleId());
            list.add(sysRolePermission);
        }
        sysRolePermissionMapper.removeByRoleId(vo.getRoleId());
        int count = sysRolePermissionMapper.batchRolePermission(list);
        if (count == 0) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
    }

    @Override
    public int removeByPermissionId(Long permissionId) {
        return sysRolePermissionMapper.removeByPermissionId(permissionId);
    }

    @Override
    public List<Long> getRoleIds(Long permissionId) {

        return sysRolePermissionMapper.getRoleIds(permissionId);
    }

    @Override
    public List<Long> getPermissionIdsByRoleId(Long roleId) {

        return sysRolePermissionMapper.getPermissionIdsByRoleId(roleId);
    }
}
