package com.diabetes.notes.controller;

import com.diabetes.notes.client.PatientClient;
import com.diabetes.notes.Dto.PatientDto;
import com.diabetes.notes.model.Note;
import com.diabetes.notes.repository.NoteRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller pour la gestion des notes.
 * <p>
 * Ce contrôleur expose des endpoints REST pour :
 * <ul>
 *   <li>Récupérer la liste des notes pour un patient donné.</li>
 *   <li>Ajouter une note après vérification de l'existence du patient via un client Feign.</li>
 * </ul>
 * </p>
 */
@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private static final Logger logger = LoggerFactory.getLogger(NoteController.class);

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private PatientClient patientClient;

    /**
     * Récupère la liste des notes associées à un patient.
     *
     * @param patientId l'identifiant du patient dont on souhaite obtenir les notes
     * @return la liste des notes du patient
     */
    @GetMapping("/patient/{patientId}")
    public List<Note> getNotesByPatientId(@PathVariable String patientId) {
        logger.info("Récupération des notes pour le patient ID: {}", patientId);
        return noteRepository.findByPatientId(patientId);
    }

    /**
     * Ajoute une nouvelle note pour un patient, après avoir vérifié que le patient existe via le client Feign.
     * <p>
     * Si le patient n'est pas trouvé, renvoie un statut 404 avec le message "Patient non trouvé".
     * En cas d'erreur lors de la vérification, renvoie un statut 500.
     * </p>
     *
     * @param note la note à ajouter, contenant l'ID du patient et le contenu
     * @return la note enregistrée ou une réponse d'erreur appropriée
     */
    @PostMapping
    public ResponseEntity<?> addNote(@RequestBody Note note) {
        logger.info("Tentative d'ajout d'une note pour le patient ID: {}", note.getPatientId());
        // Vérifier si le patient existe via le client Feign
        try {
            ResponseEntity<PatientDto> patientResponse = patientClient.getPatientById(note.getPatientId());
            if (!patientResponse.getStatusCode().is2xxSuccessful() || patientResponse.getBody() == null) {
                logger.warn("Patient non trouvé pour l'ID: {}", note.getPatientId());
                return ResponseEntity.status(404).body("Patient non trouvé");
            }
        } catch (FeignException.NotFound ex) {
            logger.warn("Patient non trouvé (exception) pour l'ID: {}", note.getPatientId());
            return ResponseEntity.status(404).body("Patient non trouvé");
        } catch (Exception ex) {
            logger.error("Erreur lors de la vérification du patient pour l'ID {}: {}", note.getPatientId(), ex.getMessage());
            return ResponseEntity.status(500).body("Erreur lors de la vérification du patient");
        }
        // Définir la date de création de la note et la sauvegarder
        note.setCreatedAt(LocalDateTime.now());
        Note savedNote = noteRepository.save(note);
        logger.info("Note ajoutée avec succès pour le patient ID: {}", note.getPatientId());
        return ResponseEntity.ok(savedNote);
    }
}