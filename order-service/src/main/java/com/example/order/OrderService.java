package com.example.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.order.config.ServiceConfiguration;

@SpringBootApplication
public class OrderService {
	public static void main(String[] args) {
		SpringApplication.run(ServiceConfiguration.class, args);
	}
}