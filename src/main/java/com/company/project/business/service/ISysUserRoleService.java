package com.company.project.business.service;


import com.company.project.business.vo.user.UserRoleOperationReqVO;
import com.company.project.framework.object.IService;
import com.company.project.persistence.beans.SysUser;
import com.company.project.persistence.beans.SysUserRole;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author pingxin
 * @since 2020-03-03
 */
public interface ISysUserRoleService extends IService<SysUserRole> {

    int removeByRoleId(Long roleId);

    List<Long> getRoleIdsByUserId(Long userId);


    void addUserRoleInfo(UserRoleOperationReqVO vo);

    int removeByUserId(Long userId);


    List<Long> getUserIdsByRoleIds(List<Long> roleIds);

    List<SysUser> selectUserInfoByRoleId(Long roleIds);


}
