package com.diabetes.risk.client;

import com.diabetes.risk.dto.PatientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.diabetes.risk.config.PatientsFeignConfig;

/**
 * Client Feign pour communiquer avec le microservice patient.
 */
@FeignClient(name = "patient-service", url = "${patient.service.url:http://patient-service:8081}",  configuration = PatientsFeignConfig.class)
public interface PatientClient {

    /**
     * Récupère les informations d'un patient par son identifiant.
     *
     * @param id l'identifiant du patient
     * @return la réponse contenant les informations du patient
     */
    @GetMapping("/{id}")
    ResponseEntity<PatientDto> getPatientById(@PathVariable("id") String id);
} 