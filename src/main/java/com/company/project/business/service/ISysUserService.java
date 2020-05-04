package com.company.project.business.service;



import com.company.project.business.vo.user.*;
import com.company.project.framework.object.IService;
import com.company.project.framework.object.PageResult;
import com.company.project.persistence.beans.SysUser;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author pingxin
 * @since 2020-03-03
 */
public interface ISysUserService extends IService<SysUser> {

    Long register(RegisterReqVO vo);

    void updateUserInfo(UserUpdateReqVO vo, Long operationId);


    PageResult<SysUser> pageInfo(UserPageReqVO vo);

    SysUser detailInfo(Long userId);


    void addUser(UserAddReqVO vo);

    void updatePwd(UpdatePasswordReqVO vo, Long userId);

    void deletedUsers(List<Long> userIds, String operationId);

    UserOwnRoleRespVO getUserOwnRole(Long userId);

    void setUserOwnRole(Long userId, List<Long> roleIds);

    SysUser getByUserName(String username);

    String loadAccountRole(Long appId);
}
