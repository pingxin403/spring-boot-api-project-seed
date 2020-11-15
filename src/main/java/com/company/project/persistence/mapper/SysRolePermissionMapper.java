package com.company.project.persistence.mapper;


import com.company.project.persistence.beans.SysRolePermission;
import com.company.project.plugin.myabtis.BaseMapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author pingxin
 * @since 2020-03-03
 */
public interface SysRolePermissionMapper extends BaseMapper<SysRolePermission> {


    int removeByRoleId(Long roleId);

    List<Long> getPermissionIdsByRoles(List<Long> roleIds);

    int batchRolePermission(List<SysRolePermission> list);

    int removeByPermissionId(Long permissionId);

    List<Long> getRoleIds(Long permissionId);

    List<Long> getPermissionIdsByRoleId(Long roleId);

}
