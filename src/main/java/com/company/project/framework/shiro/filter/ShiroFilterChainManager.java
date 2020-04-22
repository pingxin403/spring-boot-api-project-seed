package com.company.project.framework.shiro.filter;


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
