package com.diabetes.front.controller;

import com.diabetes.front.client.NotesClient;
import com.diabetes.front.client.PatientClient;
import com.diabetes.front.dto.NoteDto;
import com.diabetes.front.dto.PatientDto;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Contrôleur Front pour la gestion des notes.
 * Permet d'afficher l'historique des notes d'un patient et d'ajouter une nouvelle note via les FeignClients.
 */

@Controller
public class NotesFrontController {

    private static final Logger logger = LoggerFactory.getLogger(NotesFrontController.class);

    @Autowired
    private NotesClient notesClient;

    @Autowired
    private PatientClient patientClient;

    /**
     * Affiche l'historique des notes d'un patient.
     */
    @GetMapping("/patients/{id}/notes")
    public String viewPatientNotes(@PathVariable("id") String patientId,
                                   Model model,
                                   HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        try {
            logger.info("Récupération des notes pour le patient ID: {}", patientId);
            List<NoteDto> notes = notesClient.getNotesByPatientId(patientId);
            model.addAttribute("notes", notes);

            logger.info("Récupération des informations du patient pour l'ID: {}", patientId);
            PatientDto patient = patientClient.getPatientById(patientId).getBody();
            if (patient != null) {
                model.addAttribute("patientId", patient.getId());
                model.addAttribute("patientPrenom", patient.getPrenom());
                model.addAttribute("patientNom", patient.getNom());
            }
            return "patientNotes";
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des notes: {}", e.getMessage());
            model.addAttribute("error", "Erreur lors de la récupération des notes");
            return "redirect:/patients";
        }
    }

    /**
     * Traite l'ajout d'une note pour un patient.
     */
    @PostMapping("/patients/{id}/notes")
    public String addPatientNote(@PathVariable("id") String patientId,
                                 @RequestParam("content") String content,
                                 Model model,
                                 HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        try {
            // Création du DTO de note
            NoteDto note = new NoteDto();
            note.setPatientId(patientId);
            note.setContent(content);

            // Utilisation du FeignClient pour envoyer la note
            notesClient.addNote(note);

            return "redirect:/patients/" + patientId + "/notes";
        } catch (Exception e) {
            logger.error("Erreur lors de l'ajout de la note: {}", e.getMessage());
            model.addAttribute("error", "Erreur lors de l'ajout de la note");
            return "patientNotes";
        }
    }
}
