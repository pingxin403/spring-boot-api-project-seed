package com.company.project.business.service;


import com.company.project.business.vo.permission.PermissionAddReqVO;
import com.company.project.business.vo.permission.PermissionPageReqVO;
import com.company.project.business.vo.permission.PermissionRespNode;
import com.company.project.business.vo.permission.PermissionUpdateReqVO;
import com.company.project.framework.object.IService;
import com.company.project.framework.object.PageResult;
import com.company.project.persistence.beans.SysPermission;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author pingxin
 * @since 2020-03-03
 */
public interface ISysPermissionService extends IService<SysPermission> {
    List<SysPermission> getPermission(Long userId);

    SysPermission addPermission(PermissionAddReqVO vo);

    SysPermission detailInfo(Long permissionId);

    void updatePermission(PermissionUpdateReqVO vo);

    void deleted(Long permissionId);

    PageResult<SysPermission> pageInfo(PermissionPageReqVO vo);

    List<SysPermission> selectAll();

    Set<String> getPermissionsByUserId(Long userId);

    List<PermissionRespNode> permissionTreeList(Long userId);

    List<PermissionRespNode> selectAllByTree();

    List<PermissionRespNode> selectAllMenuByTree(Long permissionId);
}
