package com.company.project.persistence.mapper;


import com.company.project.persistence.beans.SysUser;
import com.company.project.plugin.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author pingxin
 * @since 2020-03-03
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    SysUser getUserInfoByName(String username);


    int deletedUsers(@Param("sysUser") SysUser sysUser, @Param("list") List<Long> list);

    String selectUserRoles(Long appId) throws DataAccessException;

}
