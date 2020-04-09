package com.company.project.persistence.mapper;

import com.company.project.persistence.beans.SysUserRole;
import com.company.project.plugin.BaseMapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author pingxin
 * @since 2020-03-03
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    int removeByRoleId(Long roleId);

    List<Long> getRoleIdsByUserId(Long userId);

    int removeByUserId(Long userId);

    List<Long> getInfoByUserIdByRoleId(Long roleId);

    List<Long> getUserIdsByRoleIds(List<Long> roleIds);

    int batchUserRole(List<SysUserRole> list);

}
