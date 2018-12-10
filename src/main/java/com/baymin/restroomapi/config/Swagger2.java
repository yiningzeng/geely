package com.baymin.restroomapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.File;
import java.util.List;

/**
 * Created by baymin on 17-8-7.
 */


@Configuration
@EnableSwagger2
public class Swagger2 implements WebMvcConfigurer {

    @Value("${img-path}")
    private String imgPath;


    @Value("${swagger.basepackage}")
    private String basepackage;
    @Value("${swagger.title}")
    private String title;
    @Value("${swagger.terms-of-service-url}")
    private String termsOfServiceUrl;
    @Value("${swagger.contact}")
    private String contact;
    @Value("${swagger.version}")
    private String version;
    @Value("${swagger.description}")
    private String description;

    @Bean
    public Docket createRestApi() {

        //图片保存的目录生成也放这了
        File dir = new File(imgPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(basepackage))
                .paths(PathSelectors.any())
                .build();

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .termsOfServiceUrl(termsOfServiceUrl)
                .version(version)
                .description(description.replace("$","\n"))
                .contact(new Contact("baymin","termsOfServiceUrl","zengwei@gailileo-ai.com"))
                .build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/statics/**").addResourceLocations("classpath:/statics/");
        // 解决 SWAGGER 404报错
        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

    }
}
