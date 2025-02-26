package com.diabetes.risk.controller;

import com.diabetes.risk.dto.NoteDto;
import com.diabetes.risk.dto.PatientDto;
import com.diabetes.risk.service.RiskAssessmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/risk")
public class RiskAssessmentController {

    private static final Logger logger = LoggerFactory.getLogger(RiskAssessmentController.class);
    private final RiskAssessmentService riskAssessmentService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${patient.service.url:http://patient-service:8081/api/patients}")
    private String patientServiceUrl;

    @Value("${notes.service.url:http://notes-service:8083/api/notes}")
    private String notesServiceUrl;

    // Identifiants pour Basic Auth lors de l'appel du notes-service (si nécessaire)
    @Value("${notes.service.user:admin}")
    private String notesServiceUser;
    @Value("${notes.service.password:1234}")
    private String notesServicePassword;

    public RiskAssessmentController(RiskAssessmentService riskAssessmentService) {
        this.riskAssessmentService = riskAssessmentService;
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<?> getRiskReport(@PathVariable String patientId) {
        try {
            // Récupérer les informations du patient
            String patientUrl = patientServiceUrl + "/" + patientId;
            ResponseEntity<PatientDto> patientResponse = restTemplate.getForEntity(patientUrl, PatientDto.class);
            PatientDto patient = patientResponse.getBody();

            if (patient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient non trouvé");
            }

            // Récupérer les notes du patient en ajoutant l'en-tête Basic Auth pour le notes-service
            String notesUrl = notesServiceUrl + "/patient/" + patientId;
            HttpHeaders headers = new HttpHeaders();
            String auth = notesServiceUser + ":" + notesServicePassword;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
            headers.set("Authorization", "Basic " + encodedAuth);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<List<NoteDto>> notesResponse = restTemplate.exchange(
                    notesUrl,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<NoteDto>>() {}
            );
            List<NoteDto> notes = notesResponse.getBody();

            // Calculer le risque
            RiskAssessmentService.RiskLevel risk = riskAssessmentService.assessRisk(patient, notes);
            int triggerCount = riskAssessmentService.countTriggers(notes);

            // Préparer le rapport en incluant l'ID, le prénom et le nom du patient
            Map<String, Object> report = new HashMap<>();
            report.put("patientId", patient.getId());
            report.put("patientPrenom", patient.getPrenom());
            report.put("patientNom", patient.getNom());
            report.put("riskLevel", risk);
            report.put("triggerCount", triggerCount);
            report.put("patient", patient);
            report.put("notes", notes);

            return ResponseEntity.ok(report);
        } catch (Exception e) {
            logger.error("Erreur lors de l'évaluation du risque pour le patient {}: {}", patientId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'évaluation du risque");
        }
    }
}
