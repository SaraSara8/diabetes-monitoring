package com.diabetes.risk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.diabetes.risk.client")
public class RiskAssessmentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RiskAssessmentServiceApplication.class, args);
	}

} 