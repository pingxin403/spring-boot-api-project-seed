package com.company.project.framework.property;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * redis属性配置文件
 *
 * @author hyp
 * Project name is spring-boot-learn
 * Include in com.hyp.learn.shiro.framework.property
 * hyp create at 20-3-28
 **/
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Data
@EqualsAndHashCode(callSuper = false)
@Order(-1)
public class RedisProperties {
    private Integer database = 2;
    private String host = "127.0.0.1";
    private Integer port = 6379;
    private String password = "";
    private Integer timeout=5000;
    /**
     * 默认30天 = 2592000s
     */
    private Integer expire = 2592000;

}
