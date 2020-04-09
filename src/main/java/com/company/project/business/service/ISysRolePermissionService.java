package com.company.project.business.service;



import com.company.project.business.vo.role.RolePermissionOperationReqVO;
import com.company.project.framework.object.IService;
import com.company.project.persistence.beans.SysRolePermission;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author pingxin
 * @since 2020-03-03
 */
public interface ISysRolePermissionService extends IService<SysRolePermission> {
    int removeByRoleId(Long roleId);

    List<Long> getPermissionIdsByRoles(List<Long> roleIds);

    void addRolePermission(RolePermissionOperationReqVO vo);

    int removeByPermissionId(Long permissionId);

    List<Long> getRoleIds(Long permissionId);

    List<Long> getPermissionIdsByRoleId(Long roleId);

}
