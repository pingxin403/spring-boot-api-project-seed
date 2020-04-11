package com.company.project.framework.shiro.filter;


import com.company.project.business.service.ISysUserService;
import com.company.project.business.service.RedisService;
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
        JwtFilter jwtFilter = new JwtFilter();
        filters.put("jwt", jwtFilter);
        return filters;
    }

}
