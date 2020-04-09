package com.company.project.framework.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 应用属性类
 *
 * @author hyp
 * Project name is blog
 * Include in com.hyp.learn.core.framework.property
 * hyp create at 20-3-18
 **/
@Component
@ConfigurationProperties(prefix = "app")
@Data
public class AppProperties {

    /**
     * 是否启用验证码
     */
    public boolean enableKaptcha = false;

    public boolean enableEncryptPassword=true;

}
