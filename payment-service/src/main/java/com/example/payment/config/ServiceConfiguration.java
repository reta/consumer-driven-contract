package com.example.payment.config;

import java.util.Collections;
import java.util.List;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.openapi.OpenApiFeature;
import org.apache.cxf.jaxrs.swagger.ui.SwaggerUiConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.example.payment.web.PaymentResource;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

@Configuration
@EnableAutoConfiguration
public class ServiceConfiguration {
	@Bean @DependsOn("cxf")
	Server jaxRsServer(SpringBus bus) {
		final JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();

		factory.setServiceBean(paymentResource());
		factory.setProvider(new JacksonJsonProvider());
		factory.setFeatures(List.of(openApiFeature()));
		factory.setBus(bus);
		factory.setAddress("/");

		return factory.create();
	}
	
	@Bean
	PaymentResource paymentResource() {
	    return new PaymentResource();
	}
	
    @Bean
    OpenApiFeature openApiFeature() {
        final OpenApiFeature openApiFeature = new OpenApiFeature();
        openApiFeature.setResourcePackages(Collections.singleton("com.example.payment.web"));
        openApiFeature.setPrettyPrint(true);
        openApiFeature.setTitle("Payment Service");
        openApiFeature.setContactName("Payment Service team");
        openApiFeature.setDescription("Payment Service APIs");
        openApiFeature.setVersion("0.0.1-SNAPSHOT");
        openApiFeature.setSwaggerUiConfig(new SwaggerUiConfig().url("/services/openapi.json"));
        return openApiFeature;
    }

}
