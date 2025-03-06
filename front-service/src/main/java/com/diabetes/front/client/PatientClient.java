package com.diabetes.front.client;

import com.diabetes.front.dto.PatientDto;
import com.diabetes.front.dto.PageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Client Feign pour le service patient.
 * Fournit des méthodes pour récupérer, créer et mettre à jour des patients via l'API REST du microservice patient-service.
 */


@FeignClient(name = "patient-service", url = "${patient.service.url:http://patient-service:8081/api/patients}")
public interface PatientClient {

    @GetMapping
    ResponseEntity<PageDto<PatientDto>> getPatients(@RequestParam("page") int page,
                                                    @RequestParam("size") int size,
                                                    @RequestParam(value = "query", required = false) String query);

    @PostMapping
    ResponseEntity<PatientDto> createPatient(@RequestBody PatientDto patientDto);

    @GetMapping("/{id}")
    ResponseEntity<PatientDto> getPatientById(@PathVariable("id") String id);

    @PutMapping("/{id}")
    ResponseEntity<PatientDto> updatePatient(@PathVariable("id") String id, @RequestBody PatientDto patientDto);
}
