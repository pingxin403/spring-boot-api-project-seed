package com.company.project.framework.shiro.config;


import com.company.project.business.service.IShiroService;
import com.company.project.framework.property.JwtProperties;
import com.company.project.framework.shiro.filter.ShiroFilterChainManager;
import com.company.project.framework.shiro.filter.StatelessWebSubjectFactory;
import com.company.project.framework.shiro.realm.AonModularRealmAuthenticator;
import com.company.project.framework.shiro.realm.RealmManager;
import com.company.project.util.JwtTokenUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * 配置支持JWT
 *
 * @author hyp
 */
@Configuration
public class ShiroConfig {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private IShiroService shiroService;


    @Bean("shiroSecurityManager")
    public DefaultWebSecurityManager securityManager(RealmManager realmManager) {

        //JwtTokenUtil
        JwtTokenUtil.setTokenSettings(jwtProperties);

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        List<Realm> realms = realmManager.initGetRealm();

        AonModularRealmAuthenticator authenticator = new AonModularRealmAuthenticator();
        authenticator.setRealms(realms);

        securityManager.setAuthenticator(authenticator);

        securityManager.setRealms(realms);


        /*
         * 关闭shiro自带的session，详情见文档
         * http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();

        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();

        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);

        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);

        securityManager.setSubjectDAO(subjectDAO);

        StatelessWebSubjectFactory subjectFactory = new StatelessWebSubjectFactory();
        securityManager.setSubjectFactory(subjectFactory);


        SecurityUtils.setSecurityManager(securityManager);

        return securityManager;
    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("shiroSecurityManager") SecurityManager securityManager, ShiroFilterChainManager filterChainManager) {
        RestShiroFilterFactoryBean shiroFilterFactoryBean = new RestShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //自定义拦截器限制并发人数,参考博客：
        shiroFilterFactoryBean.setFilters(filterChainManager.initGetFilters());


        // 配置数据库中的resource
        Map<String, String> map = shiroService.loadFilterChainDefinitions();

        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }


    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("shiroSecurityManager") SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }
}
