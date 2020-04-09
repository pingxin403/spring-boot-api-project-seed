package com.company.project.framework.shiro.filter;


import com.company.project.business.service.IShiroService;
import com.company.project.business.service.ISysUserService;
import com.company.project.business.service.RedisService;
import com.company.project.framework.holder.SpringContextHolder;
import com.company.project.framework.shiro.config.RestPathMatchingFilterChainResolver;
import com.company.project.framework.shiro.provider.ShiroFilterRulesProvider;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Filter 管理器
 */
@Component
public class ShiroFilterChainManager {

    @Autowired
    private RedisService redisService;
    @Autowired
    private ISysUserService accountService;

    @Value("${app.enableEncryptPassword}")
    private boolean isEncryptPassword;

    /**
     * description 初始化获取过滤链
     *
     * @return java.util.Map<java.lang.String, javax.servlet.Filter>
     */
    public Map<String, Filter> initGetFilters() {
        Map<String, Filter> filters = new LinkedHashMap<>();

        filters.put("captcha", new CaptchaValidateFilter());

        PasswordFilter passwordFilter = new PasswordFilter();
        passwordFilter.setRedisService(redisService);
        passwordFilter.setEncryptPassword(isEncryptPassword);
        filters.put("auth", passwordFilter);

        JwtFilter jwtFilter = new JwtFilter();
        jwtFilter.setRedisService(redisService);
        jwtFilter.setUserService(accountService);
        filters.put("jwt", jwtFilter);
        return filters;
    }

}
