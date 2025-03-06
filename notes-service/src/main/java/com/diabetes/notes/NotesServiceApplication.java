package com.diabetes.notes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**
 * Classe principale de l'application Notes Service.
 *
 * <p>
 * Cette classe démarre l'application Spring Boot et active les clients Feign grâce à
 * {@code @EnableFeignClients}, facilitant ainsi la communication avec d'autres microservices.
 * </p>
 */

@SpringBootApplication
@EnableFeignClients   // Active les clients Feign
public class NotesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotesServiceApplication.class, args);
	}

}