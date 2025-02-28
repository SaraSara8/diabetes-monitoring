package com.diabetes.risk.controller;

import com.diabetes.risk.client.PatientClient;
import com.diabetes.risk.client.NotesClient;
import com.diabetes.risk.dto.NoteDto;
import com.diabetes.risk.dto.PatientDto;
import com.diabetes.risk.service.RiskAssessmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller pour l'évaluation des risques de diabète.
 * Ce contrôleur interroge via Feign le microservice patient et le microservice notes
 * afin de vérifier l'existence du patient et récupérer ses notes avant de calculer le risque.
 */
@RestController
@RequestMapping("/api/risk")
public class RiskAssessmentController {

    private static final Logger logger = LoggerFactory.getLogger(RiskAssessmentController.class);

    private final RiskAssessmentService riskAssessmentService;
    private final PatientClient patientClient;
    private final NotesClient notesClient;

    public RiskAssessmentController(RiskAssessmentService riskAssessmentService, PatientClient patientClient, NotesClient notesClient) {
        this.riskAssessmentService = riskAssessmentService;
        this.patientClient = patientClient;
        this.notesClient = notesClient;
    }

    /**
     * Récupère le rapport de risque de diabète pour un patient.
     *
     * Avant de calculer le risque, vérifie que le patient existe et récupère ses notes.
     *
     * @param patientId l'identifiant du patient
     * @return un rapport sous forme de Map contenant les informations du patient, le niveau de risque, le nombre de déclencheurs, etc.
     */
    @GetMapping("/{patientId}")
    public ResponseEntity<?> getRiskReport(@PathVariable String patientId) {
        try {
            // Vérifier l'existence du patient via le client Feign
            ResponseEntity<PatientDto> patientResponse = patientClient.getPatientById(patientId);
            if (!patientResponse.getStatusCode().is2xxSuccessful() || patientResponse.getBody() == null) {
                logger.warn("Patient non trouvé pour l'ID: {}", patientId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient non trouvé");
            }
            PatientDto patient = patientResponse.getBody();

            // Récupérer les notes du patient via le client Feign
            ResponseEntity<List<NoteDto>> notesResponse = notesClient.getNotesByPatientId(patientId);
            if (!notesResponse.getStatusCode().is2xxSuccessful() || notesResponse.getBody() == null) {
                logger.warn("Notes non trouvées pour le patient ID: {}", patientId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notes non trouvées pour le patient");
            }
            List<NoteDto> notes = notesResponse.getBody();

            // Calculer le risque de diabète
            RiskAssessmentService.RiskLevel risk = riskAssessmentService.assessRisk(patient, notes);
            int triggerCount = riskAssessmentService.countTriggers(notes);

            // Préparer le rapport de risque
            Map<String, Object> report = new HashMap<>();
            report.put("patientId", patient.getId());
            report.put("patientPrenom", patient.getPrenom());
            report.put("patientNom", patient.getNom());
            report.put("riskLevel", risk);
            report.put("triggerCount", triggerCount);
            report.put("patient", patient);
            report.put("notes", notes);

            logger.info("Rapport de risque généré pour le patient ID: {}", patientId);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            logger.error("Erreur lors de l'évaluation du risque pour le patient {}: {}", patientId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'évaluation du risque");
        }
    }
}