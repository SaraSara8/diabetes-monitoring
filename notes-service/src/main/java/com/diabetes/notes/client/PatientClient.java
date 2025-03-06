package com.diabetes.notes.client;

import com.diabetes.notes.Dto.PatientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.diabetes.notes.config.PatientsFeignConfig;


/**
 * Client Feign pour communiquer avec le microservice "patient-service".
 *
 * <p>
 * Cette interface définit une méthode pour récupérer les informations d'un patient par son identifiant
 * via une requête GET. Elle utilise la configuration {@link com.diabetes.notes.config.PatientsFeignConfig}
 * pour ajouter automatiquement un header d'authentification Basic aux requêtes.
 * </p>
 *
 * <p>
 * Le client est nommé "patient-service" et utilise l'URL définie par la propriété
 * "patient.service.url" (avec une valeur par défaut de "http://patient-service:8081").
 * </p>
 *
 * @see com.diabetes.notes.config.PatientsFeignConfig
 */
@FeignClient(name = "patient-service", url = "${patient.service.url:http://patient-service:8081}",  configuration = PatientsFeignConfig.class)
public interface PatientClient {


    @GetMapping("/{id}")
    ResponseEntity<PatientDto> getPatientById(@PathVariable("id") String id);
}