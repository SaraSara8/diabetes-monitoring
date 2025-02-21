package com.diabetes.patient;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.diabetes.patient.repository.PatientRepository;
import com.diabetes.patient.model.Patient;

/**
 * Classe principale pour démarrer le microservice patient-service.
 */
@SpringBootApplication
public class PatientServiceApplication {

	/**
	 * Méthode main de l'application, point d'entrée du microservice.
	 *
	 * @param args arguments de la ligne de commande
	 */
	public static void main(String[] args) {
		SpringApplication.run(PatientServiceApplication.class, args);
	}


}
