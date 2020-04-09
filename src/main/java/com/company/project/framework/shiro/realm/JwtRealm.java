package com.company.project.framework.shiro.realm;

import com.company.project.business.consts.JwtConstant;
import com.company.project.business.service.ISysPermissionService;
import com.company.project.business.service.ISysRoleService;
import com.company.project.business.service.RedisService;
import com.company.project.framework.shiro.token.JwtToken;
import com.company.project.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.Collection;
import java.util.List;

@Slf4j
public class JwtRealm extends AuthorizingRealm {
    @Autowired
    @Lazy
    private RedisService redisService;
    @Autowired
    @Lazy
    private ISysPermissionService permissionService;
    @Autowired
    @Lazy
    private ISysRoleService roleService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        String accessToken = (String) SecurityUtils.getSubject().getPrincipal();
        Long userId = Long.parseLong(JwtTokenUtil.getUserId(accessToken));
        log.info("userId={}", userId);
        if (redisService.hasKey(JwtConstant.JWT_REFRESH_KEY + userId) && redisService.getExpire(JwtConstant.JWT_REFRESH_KEY + userId) > JwtTokenUtil.getRemainingTime(accessToken)) {
            List<String> roleNames = roleService.getRoleNames(userId);
            if (roleNames != null && !roleNames.isEmpty()) {
                authorizationInfo.addRoles(roleService.getRoleNames(userId));
            }
            authorizationInfo.setStringPermissions(permissionService.getPermissionsByUserId(userId));
        } else {
            Claims claimsFromToken = JwtTokenUtil.getClaimsFromToken(accessToken);
            if (claimsFromToken.get(JwtConstant.JWT_ROLES_KEY) != null) {
                authorizationInfo.addRoles((Collection<String>) claimsFromToken.get(JwtConstant.JWT_ROLES_KEY));
            }
            if (claimsFromToken.get(JwtConstant.JWT_PERMISSIONS_KEY) != null) {
                authorizationInfo.addStringPermissions((Collection<String>) claimsFromToken.get(JwtConstant.JWT_PERMISSIONS_KEY));
            }

        }
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        JwtToken token = (JwtToken) authenticationToken;
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(token.getPrincipal(), token.getPrincipal(), getName());
        return simpleAuthenticationInfo;
    }
}
