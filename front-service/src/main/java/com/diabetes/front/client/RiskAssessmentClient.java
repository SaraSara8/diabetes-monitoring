package com.diabetes.front.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Map;

/**
 * Client Feign pour interroger le microservice risk-assessment.
 */
@FeignClient(name = "risk-assessment-service", url = "${risk.service.url:http://risk-assessment-service:8084/api/risk}")
public interface RiskAssessmentClient {

    /**
     * Récupère le rapport de risque pour un patient.
     *
     * @param patientId l'identifiant du patient
     * @return une Map contenant le rapport de risque
     */
    @GetMapping("/{patientId}")
    Map<String, Object> getRiskReport(@PathVariable("patientId") String patientId);
}