package com.company.project.framework.property;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author hyp
 * Project name is blog
 * Include in com.hyp.learn.core.framework.property
 * hyp create at 20-3-18
 **/
@Component
@ConfigurationProperties(prefix = "app.shiro")
@Data
@EqualsAndHashCode(callSuper = false)
@Order(-1)
public class ShiroProperties {

    public String loginUrl = "/passport/login";
    public String successUrl = "/";
    public String unauthorizedUrl = "/error/403";


}
