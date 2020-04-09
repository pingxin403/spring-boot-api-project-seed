package com.company.project.framework.config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * 配置 Hibernate 参数校验
 * 参考：https://blog.csdn.net/u010454030/article/details/53009327
 *
 * @author hyp
 * Project name is blog
 * Include in com.hyp.learn.core.framework.config
 * hyp create at 20-3-18
 **/
@Configuration
@EnableAutoConfiguration
public class ValidatorConfig {
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor postProcessor = new MethodValidationPostProcessor();
        postProcessor.setValidator(validator());//快速校验，只要有错马上返回
        return postProcessor;
    }

    @Bean
    public javax.validation.Validator validator() {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .addProperty("hibernate.validator.fail_fast", "true")
                .buildValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        return validator;
    }

}
