package com.company.project.framework.config;

import com.company.project.business.consts.ProjectConstant;
import com.company.project.framework.exception.code.BaseResponseCode;
import com.company.project.framework.object.ResponseVO;
import com.company.project.util.ResultUtil;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author hyp
 * Project name is spring-boot-learn
 * Include in com.hyp.learn.w.config
 * hyp create at 19-12-22
 **/
@Configuration
@EnableSwagger2 // 标记项目启用 Swagger API 接口文档
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {

    @Value("${swagger2.enable}")
    private boolean enable = true;

    @Bean
    public Docket createRestApi() {

        //添加全局响应状态码
        List<ResponseMessage> responseMessageList = new ArrayList<>();
        Arrays.stream(BaseResponseCode.values()).forEach(errorEnums -> {
            responseMessageList.add(
                    new ResponseMessageBuilder()
                            .code(errorEnums.getCode())
                            .message(errorEnums.getMsg())
                            .responseModel(new ModelRef("ResponseVO"))
                            .build()
            );
        });


        // 创建 Docket 对象
        return new Docket(DocumentationType.SWAGGER_2) // 文档类型，使用 Swagger2
                // 添加全局响应状态码
                .globalResponseMessage(RequestMethod.GET, responseMessageList)
                .globalResponseMessage(RequestMethod.POST, responseMessageList)
                .globalResponseMessage(RequestMethod.PUT, responseMessageList)
                .globalResponseMessage(RequestMethod.DELETE, responseMessageList)
                .apiInfo(this.apiInfo()) // 设置 API 信息
                //分组名称
                .groupName("2.X版本")
                // 扫描 Controller 包路径，获得 API 接口
                .select()
                .apis(RequestHandlerSelectors.basePackage(ProjectConstant.BASE_PACKAGE + "." + ProjectConstant.CONTROLLER_PACKAGE))
                .paths(PathSelectors.any())
                // 构建出 Docket 对象
                .build()
                .enable(enable);
    }

    /**
     * 创建 API 信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("测试接口文档示例")
                .description("我是一段描述")
                .version("1.0.0") // 版本号
                .contact(new Contact("平心", "http://hanyunpeng0521.github.io", "m13839441583@163.com")) // 联系人
                .build();
    }


}