package com.company.project.business.service.impl;

import com.company.project.business.service.IShiroService;
import com.company.project.business.service.ISysPermissionService;
import com.company.project.business.service.ISysUserRoleService;
import com.company.project.framework.holder.SpringContextHolder;
import com.company.project.framework.shiro.realm.JwtRealm;
import com.company.project.persistence.beans.SysPermission;
import com.company.project.persistence.beans.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hyp
 * Project name is spring-boot-shiro-frame
 * Include in com.hyp.learn.shiro.business.service.impl
 * hyp create at 20-3-31
 **/
@Slf4j
@Service
public class ShiroServiceImpl implements IShiroService {

    @Autowired
    private ISysPermissionService resourcesService;
    @Autowired
    private ISysUserRoleService userRoleService;

    /**
     * 初始化权限
     */
    @Override
    public Map<String, String> loadFilterChainDefinitions() {
        /*
            配置访问权限
            - anon:所有url都都可以匿名访问
            - authc: 需要认证才能进行访问（此处指所有非匿名的路径都需要登陆才能访问）
            - user:配置记住我或认证通过可以访问
         */
        Map<String, String> filterChain = new LinkedHashMap<String, String>();
        // 配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了
        filterChain.put("/passport/logout", "logout");
        // -------------auth 默认需要认证过滤器的URL 走auth--PasswordFilter
        filterChain.put("/passport/signin", "anon");
        filterChain.put("/passport/pwd", "user");
        filterChain.put("/passport/**", "anon");

        filterChain.put("/css/**", "anon");
        filterChain.put("/js/**", "anon");


        //放开swagger-ui地址
        filterChain.put("/swagger/**", "anon");
        filterChain.put("/v2/api-docs", "anon");
        filterChain.put("/swagger-ui.html", "anon");
        filterChain.put("/swagger-resources/**", "anon");
        filterChain.put("/webjars/**", "anon");
        filterChain.put("/druid/**", "anon");
        filterChain.put("/favicon.ico", "anon");
        filterChain.put("/captcha.jpg", "anon");
        filterChain.put("/csrf", "anon");
        filterChain.put("/**", "jwt,authc");


        // 加载数据库中配置的资源权限列表
        List<SysPermission> resourcesList = resourcesService.selectAll();
        for (SysPermission resources : resourcesList) {
            if (!StringUtils.isEmpty(resources.getUrl()) && !StringUtils.isEmpty(resources.getPerms())) {
                String permission = "perms[" + resources.getPerms() + "]";
                filterChain.put(resources.getUrl(), permission);
            }
        }
        return filterChain;
    }

    /**
     * 重新加载权限
     */
    @Override
    public void updatePermission() {
        ShiroFilterFactoryBean shirFilter = SpringContextHolder.getBean(ShiroFilterFactoryBean.class);
        synchronized (shirFilter) {
            AbstractShiroFilter shiroFilter = null;
            try {
                shiroFilter = (AbstractShiroFilter) shirFilter.getObject();
            } catch (Exception e) {
                throw new RuntimeException("get ShiroFilter from shiroFilterFactoryBean error!");
            }

            PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter.getFilterChainResolver();
            DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();

            // 清空老的权限控制
            manager.getFilterChains().clear();

            shirFilter.getFilterChainDefinitionMap().clear();
            shirFilter.setFilterChainDefinitionMap(loadFilterChainDefinitions());
            // 重新构建生成
            Map<String, String> chains = shirFilter.getFilterChainDefinitionMap();
            for (Map.Entry<String, String> entry : chains.entrySet()) {
                String url = entry.getKey();
                String chainDefinition = entry.getValue().trim().replace(" ", "");
                manager.createChain(url, chainDefinition);
            }
        }
    }

    /**
     * 重新加载用户权限
     *
     * @param user
     */
    @Override
    public void reloadAuthorizingByUserId(SysUser user) {
        RealmSecurityManager rsm = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        JwtRealm shiroRealm = (JwtRealm) rsm.getRealms().iterator().next();
        Subject subject = SecurityUtils.getSubject();
        String realmName = subject.getPrincipals().getRealmNames().iterator().next();
        SimplePrincipalCollection principals = new SimplePrincipalCollection(user, realmName);
        subject.runAs(principals);
        shiroRealm.getAuthorizationCache().remove(subject.getPrincipals());
        subject.releaseRunAs();

        log.info("用户[{}]的权限更新成功！！", user.getUsername());

    }

    /**
     * 重新加载所有拥有roleId角色的用户的权限
     *
     * @param roleId
     */
    @Override
    public void reloadAuthorizingByRoleId(Long roleId) {
        List<SysUser> userList = userRoleService.selectUserInfoByRoleId(roleId);
        if (CollectionUtils.isEmpty(userList)) {
            return;
        }
        for (SysUser user : userList) {
            reloadAuthorizingByUserId(user);
        }
    }

}

