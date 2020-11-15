package com.company.project.persistence.mapper;

import com.company.project.persistence.beans.SysPermission;
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
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    List<SysPermission> selectChild(Long pid);

}
