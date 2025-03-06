package com.diabetes.front.controller;

import com.diabetes.front.client.PatientClient;
import com.diabetes.front.dto.PatientDto;
import com.diabetes.front.dto.PageDto;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


/**
 * Contrôleur Front pour la gestion des patients.
 * Fournit des endpoints pour afficher la liste paginée, ajouter, et éditer des patients via l'API patient-service.
 */

@Controller
public class PatientFrontController {

    private static final Logger logger = LoggerFactory.getLogger(PatientFrontController.class);

    @Autowired
    private PatientClient patientClient;

    /**
     * Affiche la liste paginée des patients.
     * Le patient-service renvoie un PageDto avec les clés : content, totalElements, totalPages, number, size, first et last.
     */
    @GetMapping("/patients")
    public String listPatients(@RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "10") int size,
                               @RequestParam(value = "query", required = false) String query,
                               Model model,
                               HttpSession session) {
        if (session.getAttribute("username") == null) {
            logger.warn("Aucun identifiant en session, redirection vers la page de login.");
            return "redirect:/login";
        }
        try {
            logger.info("Récupération de la liste des patients (page: {}, size: {}, query: {})", page, size, query);
            ResponseEntity<PageDto<PatientDto>> response = patientClient.getPatients(page, size, query);
            PageDto<PatientDto> patientsPage = response.getBody();
            if (patientsPage == null) {
                patientsPage = new PageDto<>();
                patientsPage.setContent(java.util.Collections.emptyList());
                patientsPage.setTotalElements(0);
                patientsPage.setTotalPages(0);
                patientsPage.setNumber(page);
                patientsPage.setSize(size);
                patientsPage.setFirst(true);
                patientsPage.setLast(true);
            }
            model.addAttribute("patientsPage", patientsPage);
            model.addAttribute("page", page);
            model.addAttribute("size", size);
            model.addAttribute("query", query);
            return "patients"; // Le template pourra accéder à ${patientsPage.content}, ${patientsPage.number}, ${patientsPage.first}, etc.
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des patients: {}", e.getMessage());
            model.addAttribute("error", "Erreur lors de la récupération des patients.");
            return "redirect:/login";
        }
    }

    /**
     * Affiche le formulaire pour ajouter un nouveau patient.
     */
    @GetMapping("/patients/add")
    public String showAddForm(Model model, HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        model.addAttribute("patient", new PatientDto());
        return "patientForm"; // Template pour ajouter ou modifier un patient
    }

    /**
     * Traite l'ajout d'un nouveau patient.
     */
    @PostMapping("/patients/add")
    public String addPatient(@ModelAttribute("patient") PatientDto patientDto,
                             Model model,
                             HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        try {
            logger.info("Ajout d'un nouveau patient: {}", patientDto);
            ResponseEntity<PatientDto> response = patientClient.createPatient(patientDto);
            return "redirect:/patients";
        } catch (Exception e) {
            logger.error("Erreur lors de l'ajout du patient: {}", e.getMessage());
            model.addAttribute("error", "Erreur lors de l'ajout du patient");
            return "patientForm";
        }
    }

    /**
     * Affiche le formulaire d'édition d'un patient.
     */
    @GetMapping("/patients/edit")
    public String showEditForm(@RequestParam String id, Model model, HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        try {
            logger.info("Récupération du patient avec l'ID: {}", id);
            ResponseEntity<PatientDto> response = patientClient.getPatientById(id);
            PatientDto patient = response.getBody();
            if (patient == null) {
                logger.error("Aucun patient trouvé pour l'ID: {}", id);
                model.addAttribute("error", "Patient introuvable");
                return "redirect:/patients";
            }
            model.addAttribute("patient", patient);
            return "patientForm"; // Template pour l'édition du patient
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du patient: {}", e.getMessage());
            model.addAttribute("error", "Erreur lors de la récupération du patient");
            return "redirect:/patients";
        }
    }

    /**
     * Traite la mise à jour d'un patient.
     */
    @PostMapping("/patients/edit")
    public String updatePatient(@ModelAttribute("patient") PatientDto patientDto,
                                Model model,
                                HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        try {
            logger.info("Mise à jour du patient: {}", patientDto);
            ResponseEntity<PatientDto> response = patientClient.updatePatient(patientDto.getId(), patientDto);
            return "redirect:/patients";
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour du patient: {}", e.getMessage());
            model.addAttribute("error", "Erreur lors de la mise à jour du patient");
            return "patientForm";
        }
    }
}
