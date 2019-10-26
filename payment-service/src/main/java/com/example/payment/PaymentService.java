package com.example.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.payment.config.ServiceConfiguration;

@SpringBootApplication
public class PaymentService {
	public static void main(String[] args) {
		SpringApplication.run(ServiceConfiguration.class, args);
	}
}