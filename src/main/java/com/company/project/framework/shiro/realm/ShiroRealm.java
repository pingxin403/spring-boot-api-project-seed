package com.company.project.framework.shiro.realm;


import com.company.project.business.enums.StatusEnum;
import com.company.project.business.enums.UserTypeEnum;
import com.company.project.business.service.ISysPermissionService;
import com.company.project.business.service.ISysRoleService;
import com.company.project.business.service.ISysUserService;
import com.company.project.persistence.beans.SysPermission;
import com.company.project.persistence.beans.SysRole;
import com.company.project.persistence.beans.SysUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Shiro-密码输入错误的状态下重试次数的匹配管理
 */
public class ShiroRealm extends AuthorizingRealm {

    @Resource
    private ISysUserService userService;
    @Resource
    private ISysPermissionService resourcesService;
    @Resource
    private ISysRoleService roleService;

    /**
     * 提供账户信息返回认证信息（用户的角色信息集合）
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取用户的输入的账号.
        String username = (String) token.getPrincipal();
        SysUser user = userService.getByUserName(username);
        if (user == null) {
            throw new UnknownAccountException("账号不存在！");
        }
        if (user.getStatus() != null && StatusEnum.LOCKED.getCode().equals(user.getStatus())) {
            throw new LockedAccountException("帐号已被锁定，禁止登录！");
        }

        // principal参数使用用户Id，方便动态刷新用户权限
        return new SimpleAuthenticationInfo(
                user.getId(),
                user.getPassword(),
                ByteSource.Util.bytes(username),
                getName()
        );
    }

    /**
     * 权限认证，为当前登录的Subject授予角色和权限（角色的权限信息集合）
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        Long userId = (Long) SecurityUtils.getSubject().getPrincipal();

        // 赋予角色
        List<SysRole> roleList = roleService.getRoleInfoByUserId(userId);
        for (SysRole role : roleList) {
            info.addRole(role.getName());
        }

        // 赋予权限
        List<SysPermission> resourcesList = null;
        SysUser user = userService.getById(userId);
        if (null == user) {
            return info;
        }
        // ROOT用户默认拥有所有权限
        if (UserTypeEnum.ROOT.toString().equalsIgnoreCase(user.getUserType())) {
            resourcesList = resourcesService.selectAll();
        } else {
            resourcesList = resourcesService.getPermission(userId);
        }

        if (!CollectionUtils.isEmpty(resourcesList)) {
            Set<String> permissionSet = new HashSet<>();
            for (SysPermission resources : resourcesList) {
                String permission = null;
                if (!StringUtils.isEmpty(permission = resources.getPerms())) {
                    permissionSet.addAll(Arrays.asList(permission.trim().split(",")));
                }
            }
            info.setStringPermissions(permissionSet);
        }
        return info;
    }

}
