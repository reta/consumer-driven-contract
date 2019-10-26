package com.example.order.config;

import java.util.Collections;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

import com.example.order.web.OrderController;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableAutoConfiguration
@EnableSwagger2
public class ServiceConfiguration {
    @Bean
    OrderController orderController() {
        return new OrderController();
    }
    
    @Bean
    Docket orderApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.order.web"))
                .paths(PathSelectors.any())
                .build()
            .pathMapping("/")
            .genericModelSubstitutes(ResponseEntity.class)
            .enableUrlTemplating(true)
            .apiInfo(apiInfo());
    }
    
    private ApiInfo apiInfo() {
        return new ApiInfo(
            "Order Service", 
            "Order Service Web APIs", 
            "0.0.1-SNAPSHOT", 
            null, 
            null, 
            null,
            null,
            Collections.emptyList());
    }
}
