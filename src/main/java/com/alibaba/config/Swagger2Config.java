package com.alibaba.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author zycstart
 * @create 2020-05-27 21:18
 */

@Configuration  //开启扫描
@EnableSwagger2 //开启swagger2
public class Swagger2Config {

    /**
     * 用于配置swagger2，包含文档基本信息
     * 指定swagger2的作用域（这里指定包路径下的所有API）
     *
     * @return Docket
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //指定需要扫描的controller
                .apis(RequestHandlerSelectors.basePackage("com.alibaba.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 构建文档基本信息，用于页面显示，可以包含版本、
     * 联系人信息、服务地址、文档描述信息等
     *
     * @return ApiInfo
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //标题
                .title("Spring Boot2中采用Swagger2构建RESTful APIs")
                .description("通过访问swagger-ui.html,实现接口测试、文档生成")
                //设置联系方式
                .contact(new Contact("番茄、薯条", "https://blog.csdn.net/m1234ilu/article/details/106392177", "xxxxx@qq.com"))
                .version("1.0")
                .build();
    }

}
