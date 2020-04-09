package com.company.project.business.service;


import com.company.project.business.vo.role.RoleAddReqVO;
import com.company.project.business.vo.role.RolePageReqVO;
import com.company.project.business.vo.role.RoleUpdateReqVO;
import com.company.project.framework.object.IService;
import com.company.project.framework.object.PageResult;
import com.company.project.persistence.beans.SysRole;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author pingxin
 * @since 2020-03-03
 */
public interface ISysRoleService extends IService<SysRole> {

    SysRole addRole(RoleAddReqVO vo);

    void updateRole(RoleUpdateReqVO vo, String accessToken);

    SysRole detailInfo(Long id);

    void deletedRole(Long id);

    PageResult<SysRole> pageInfo(RolePageReqVO vo);

    List<SysRole> getRoleInfoByUserId(Long userId);

    List<String> getRoleNames(Long userId);

    List<SysRole> selectAllRoles();
}
