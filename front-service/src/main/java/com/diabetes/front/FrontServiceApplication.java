package com.diabetes.front;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**
 * Application principale du service Front.
 * Démarre l'application Spring Boot et active les clients Feign pour la communication inter-microservices.
 */

@SpringBootApplication
@EnableFeignClients(basePackages = "com.diabetes.front.client")
public class FrontServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FrontServiceApplication.class, args);
	}

}
