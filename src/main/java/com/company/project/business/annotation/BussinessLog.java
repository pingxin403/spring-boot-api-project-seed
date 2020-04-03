package com.company.project.business.annotation;


import com.company.project.business.enums.PlatformEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志记录、自定义注解,位置controller的方法
 *
 * @author hyp
 * Project name is blog
 * Include in com.hyp.learn.core.business.annotation
 * hyp create at 20-3-19
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BussinessLog {
    /**
     * 业务的名称
     */
    String value() default "";

    /**
     * 平台，默认为后台管理
     */
    PlatformEnum platform() default PlatformEnum.ADMIN;

    /**
     * 是否将当前日志记录到数据库中
     */
    boolean save() default true;

}
