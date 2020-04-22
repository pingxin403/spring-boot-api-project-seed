package com.company.project.framework.config;

import com.company.project.business.consts.ProjectConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hyp
 * Project name is spring-boot-learn
 * Include in com.hyp.learn.w.config
 * hyp create at 19-12-22
 **/
@Configuration
@EnableSwagger2 // 标记项目启用 Swagger API 接口文档
public class SwaggerConfig {

    @Value("${swagger2.enable}")
    private boolean enable=true;

    @Bean
    public Docket createRestApi() {
        /**
         * 这是为了我们在用 swagger 测试接口的时候添加头部信息
         */
        List<Parameter> pars = new ArrayList<Parameter>();
        ParameterBuilder tokenPar = new ParameterBuilder();
        ParameterBuilder refreshTokenPar = new ParameterBuilder();
        tokenPar.name("authorization")
                .description("swagger测试用(模拟token传入)非必填 header")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false);
        refreshTokenPar.name("refresh_token")
                .description("swagger测试用(模拟刷新token传入)非必填 header")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false);
        /**
         * 多个的时候 就直接添加到 pars 就可以了
         */
        pars.add(tokenPar.build());
        pars.add(refreshTokenPar.build());

        // 创建 Docket 对象
        return new Docket(DocumentationType.SWAGGER_2) // 文档类型，使用 Swagger2
                .apiInfo(this.apiInfo()) // 设置 API 信息
                // 扫描 Controller 包路径，获得 API 接口
                .select()
                .apis(RequestHandlerSelectors.basePackage(ProjectConstant.BASE_PACKAGE + "." + ProjectConstant.CONTROLLER_PACKAGE))
                .paths(PathSelectors.any())
                // 构建出 Docket 对象
                .build()
                .globalOperationParameters(pars)
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