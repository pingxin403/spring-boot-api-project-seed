package com.company.project.framework.shiro.config;

import com.company.project.business.service.IShiroService;
import com.company.project.framework.property.JwtProperties;
import com.company.project.framework.shiro.cache.RedisCacheManager;
import com.company.project.framework.shiro.filter.ShiroFilterChainManager;
import com.company.project.framework.shiro.matcher.ShiroHashedCredentialsMatcher;
import com.company.project.framework.shiro.realm.JwtRealm;
import com.company.project.util.JwtTokenUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

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

    @Bean
    public ShiroHashedCredentialsMatcher customHashedCredentialsMatcher() {
        return new ShiroHashedCredentialsMatcher();
    }

    @Bean
    public JwtRealm customRealm(RedisCacheManager redisCacheManager) {
        JwtRealm jwtRealm = new JwtRealm();
        jwtRealm.setCredentialsMatcher(customHashedCredentialsMatcher());
        jwtRealm.setCacheManager(redisCacheManager);
        return jwtRealm;
    }

    @Bean
    public RedisCacheManager redisCacheManager() {
        return new RedisCacheManager();
    }

    @Bean("shiroSecurityManager")
    public DefaultWebSecurityManager securityManager(JwtRealm jwtRealm) {

        //JwtTokenUtil
        JwtTokenUtil.setTokenSettings(jwtProperties);

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();


        securityManager.setRealm(jwtRealm);


        /*
         * 关闭shiro自带的session，详情见文档
         * http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();

        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();

        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);

        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);

        securityManager.setSubjectDAO(subjectDAO);

//        StatelessWebSubjectFactory subjectFactory = new StatelessWebSubjectFactory();
//        securityManager.setSubjectFactory(subjectFactory);


        SecurityUtils.setSecurityManager(securityManager);

        return securityManager;
    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("shiroSecurityManager") SecurityManager securityManager, ShiroFilterChainManager filterChainManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
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

    /**
     * SpringShiroFilter首先注册到spring容器
     * 然后被包装成FilterRegistrationBean
     * 最后通过FilterRegistrationBean注册到servlet容器
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean delegatingFilterProxy() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName("shiroFilter");
        filterRegistrationBean.setFilter(proxy);
        return filterRegistrationBean;
    }
}
