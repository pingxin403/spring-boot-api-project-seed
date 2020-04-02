package com.company.project.framework.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.company.project.framework.property.DruidProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Druid Monitor 配置
 *
 * @author hyp
 * Project name is spring-boot-learn
 * Include in com.hyp.learn.shiro.framework.config
 * hyp create at 20-3-28
 **/
@Configuration
public class DruidConfig {

    @Autowired
    private DruidProperties properties;

    /**
     * 配置StatViewServlet，druid的web页面
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean druidStatViewServlet() {
        ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<>(new StatViewServlet(), properties.getServletPath());

        // IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not permitted to view this page.
        List<String> denyIps = properties.getDenyIps();
        if (!CollectionUtils.isEmpty(denyIps)) {
            bean.addInitParameter("deny", StringUtils.collectionToDelimitedString(denyIps, ","));
        }

        //Ip白名单
        List<String> allowIps = properties.getAllowIps();
        if (!CollectionUtils.isEmpty(allowIps)) {
            bean.addInitParameter("allow", StringUtils.collectionToDelimitedString(allowIps, ","));
        }

        // 登录查看信息的账号密码.
        bean.addInitParameter("loginUsername", properties.getUsername());
        bean.addInitParameter("loginPassword", properties.getPassword());
        // 禁用HTML页面上的"Reset All"功能（默认false）
        bean.addInitParameter("resetEnable", String.valueOf(properties.getResetEnable()));
        return bean;
    }

    /**
     * Druid的StatFilter
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean druidStatFilter() {
        FilterRegistrationBean<WebStatFilter> bean = new FilterRegistrationBean<>(new WebStatFilter());
        // 添加过滤规则
        bean.addUrlPatterns("/*");
        // 排除的url
        bean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return bean;
    }
}
