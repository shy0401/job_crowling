package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2) // Springfox 3.0.0은 SWAGGER_2와 OAS_30 둘 다 지원합니다.
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.demo.controller")) // 컨트롤러 패키지 지정
                .paths(PathSelectors.any()) // 모든 경로 포함
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Saramin API Documentation")
                .description("API documentation for Saramin data handling and job management.")
                .version("1.0.0")
                .contact(new Contact("Your Name", "https://your-website.com", "your-email@example.com"))
                .build();
    }
}
