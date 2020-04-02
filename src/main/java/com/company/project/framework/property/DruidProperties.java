package com.company.project.framework.property;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

/**
 * druid属性
 *
 * @author hyp
 * Project name is spring-boot-learn
 * Include in com.hyp.learn.shiro.framework.property
 * hyp create at 20-3-28
 **/
@Configuration
@ConfigurationProperties(prefix = "app.druid")
@Data
@EqualsAndHashCode(callSuper = false)
@Order(-1)
public class DruidProperties {
    private String username;
    private String password;
    private String servletPath = "/druid/*";
    private Boolean resetEnable = false;
    private List<String> allowIps;
    private List<String> denyIps;
}