package com.diabetes.notes.client;

import com.diabetes.notes.Dto.PatientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "patient-service", url = "${patient.service.url:http://patient-service:8081/api/patients}")
public interface PatientClient {

    @GetMapping("/{id}")
    ResponseEntity<PatientDto> getPatientById(@PathVariable("id") String id);
}