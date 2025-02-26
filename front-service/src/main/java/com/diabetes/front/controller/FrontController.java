package com.diabetes.front.controller;

import com.diabetes.front.dto.NoteDto;
import com.diabetes.front.dto.PatientDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import jakarta.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FrontController {

    private static final Logger logger = LoggerFactory.getLogger(FrontController.class);

    @Value("${spring.data.web.pageable.default-page-size}")
    private int defaultPageSize;

    @Value("${spring.data.web.pageable.max-page-size}")
    private int maxPageSize;

    // URL des microservices (à adapter selon votre environnement Docker)
    private static final String PATIENT_SERVICE_URL = "http://patient-service:8081/api/patients";
    private static final String NOTES_SERVICE_URL = "http://notes-service:8083/api/notes";
    private static final String RISK_SERVICE_URL = "http://risk-assessment-service:8084/api/risk";

    private final RestTemplate restTemplate = new RestTemplate();

    /*================================
         SECTION LOGIN
    ==================================*/

    @GetMapping("/login")
    public String showLoginForm() {
        logger.info("Affichage du formulaire de connexion.");
        return "login"; // Template login.html
    }

    /**
     * Connexion via Basic Auth.
     * Envoie une requête POST vers le gateway (endpoint /auth/login) pour valider les identifiants.
     * Si l'authentification est réussie, les identifiants sont stockés en session.
     */
    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               Model model,
                               HttpSession session) {
        logger.info("Tentative de connexion pour l'utilisateur: {}", username);
        String url = "http://gateway-service:8080/auth/login";
        HttpHeaders headers = new HttpHeaders();
        String auth = username + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        headers.set("Authorization", "Basic " + encodedAuth);
        HttpEntity<?> request = new HttpEntity<>(headers);
        try {
            logger.info("Envoi de la requête POST vers {}", url);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                session.setAttribute("username", username);
                session.setAttribute("password", password);
                logger.info("Authentification réussie pour l'utilisateur: {}", username);
                return "redirect:/patients";
            } else {
                logger.warn("Échec de l'authentification pour l'utilisateur: {}", username);
                model.addAttribute("error", "Authentification échouée");
                return "login";
            }
        } catch (Exception e) {
            logger.error("Erreur lors de l'authentification de l'utilisateur: {} - Message: {}", username, e.getMessage());
            model.addAttribute("error", "Identifiants invalides");
            return "login";
        }
    }

    /**
     * Déconnexion : invalide la session et redirige vers la page de login.
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    /**
     * Méthode utilitaire pour créer l'en-tête Basic Auth à partir des identifiants stockés en session.
     */
    private HttpHeaders createHeaders(HttpSession session) {
        String username = (String) session.getAttribute("username");
        String password = (String) session.getAttribute("password");
        HttpHeaders headers = new HttpHeaders();
        if (username != null && password != null) {
            String auth = username + ":" + password;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
            headers.set("Authorization", "Basic " + encodedAuth);
        }
        return headers;
    }

    /*================================
         SECTION PATIENTS
    ==================================*/

    /**
     * Affiche la liste paginée des patients avec recherche.
     */
    @GetMapping("/patients")
    public String listPatients(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "query", required = false) String query,
            Model model,
            HttpSession session) {
        logger.info("Récupération de la liste des patients (page: {}, size: {}, query: {})", page, size, query);
        if (session.getAttribute("username") == null) {
            logger.warn("Aucun identifiant en session, redirection vers la page de login.");
            return "redirect:/login";
        }
        HttpHeaders headers = createHeaders(session);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        String url = PATIENT_SERVICE_URL + "?page=" + page + "&size=" + size;
        if (query != null && !query.trim().isEmpty()) {
            url += "&query=" + query;
        }
        try {
            logger.info("Envoi de la requête GET vers {}", url);
            ResponseEntity<Map> resp = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            model.addAttribute("patientsPage", resp.getBody());
            model.addAttribute("query", query);
            model.addAttribute("page", page);
            model.addAttribute("size", size);
            return "patients"; // Template pour la liste des patients
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des patients: {}", e.getMessage());
            model.addAttribute("error", "Erreur lors de la récupération des patients.");
            return "redirect:/login";
        }
    }

    @GetMapping("/patients/add")
    public String showAddForm(Model model) {
        model.addAttribute("patient", new PatientDto());
        return "patientForm"; // Template pour ajouter/modifier un patient
    }

    @PostMapping("/patients/add")
    public String addPatient(@ModelAttribute("patient") PatientDto patientDto,
                             Model model,
                             HttpSession session) {
        logger.info("Ajout d'un nouveau patient: {}", patientDto);
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        try {
            HttpHeaders headers = createHeaders(session);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<PatientDto> request = new HttpEntity<>(patientDto, headers);
            restTemplate.postForEntity(PATIENT_SERVICE_URL, request, PatientDto.class);
            return "redirect:/patients";
        } catch (Exception e) {
            logger.error("Erreur lors de l'ajout du patient : {}", e.getMessage());
            model.addAttribute("error", "Erreur lors de l'ajout du patient");
            return "patientForm";
        }
    }

    @GetMapping("/patients/edit")
    public String showEditForm(@RequestParam String id, Model model, HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        try {
            String url = PATIENT_SERVICE_URL + "/" + id;
            HttpHeaders headers = createHeaders(session);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<PatientDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, PatientDto.class);
            model.addAttribute("patient", response.getBody());
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du patient : {}", e.getMessage());
            model.addAttribute("error", "Erreur lors de la récupération du patient");
            return "redirect:/patients";
        }
        return "patientForm";
    }

    @PostMapping("/patients/edit")
    public String updatePatient(@ModelAttribute("patient") PatientDto patientDto,
                                Model model,
                                HttpSession session) {
        logger.info("Mise à jour du patient: {}", patientDto);
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        try {
            String url = PATIENT_SERVICE_URL + "/" + patientDto.getId();
            HttpHeaders headers = createHeaders(session);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<PatientDto> request = new HttpEntity<>(patientDto, headers);
            restTemplate.exchange(url, HttpMethod.PUT, request, Void.class);
            return "redirect:/patients";
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour du patient : {}", e.getMessage());
            model.addAttribute("error", "Erreur lors de la mise à jour du patient");
            return "patientForm";
        }
    }

    /*================================
         SECTION NOTES
    ==================================*/

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
        HttpHeaders headers = createHeaders(session);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        try {
            // Récupérer les notes depuis le notes-service
            String notesUrl = NOTES_SERVICE_URL + "/patient/" + patientId;
            ResponseEntity<List<NoteDto>> notesResponse = restTemplate.exchange(
                    notesUrl,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<NoteDto>>() {}
            );
            List<NoteDto> notes = notesResponse.getBody();
            model.addAttribute("notes", notes);

            // Récupérer les informations du patient depuis le patient-service
            String patientUrl = PATIENT_SERVICE_URL + "/" + patientId;
            ResponseEntity<PatientDto> patientResponse = restTemplate.exchange(
                    patientUrl,
                    HttpMethod.GET,
                    entity,
                    PatientDto.class
            );
            PatientDto patient = patientResponse.getBody();
            if (patient != null) {
                model.addAttribute("patientId", patient.getId());
                model.addAttribute("patientPrenom", patient.getPrenom());
                model.addAttribute("patientNom", patient.getNom());
            }
            return "patientNotes"; // Votre template patientNotes.html
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
        String url = NOTES_SERVICE_URL;
        HttpHeaders headers = createHeaders(session);
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Création du DTO de note
        NoteDto note = new NoteDto();
        note.setPatientId(patientId);
        note.setContent(content);
        HttpEntity<NoteDto> request = new HttpEntity<>(note, headers);
        try {
            restTemplate.postForEntity(url, request, NoteDto.class);
            return "redirect:/patients/" + patientId + "/notes";
        } catch (Exception e) {
            logger.error("Erreur lors de l'ajout de la note: {}", e.getMessage());
            model.addAttribute("error", "Erreur lors de l'ajout de la note");
            return "patientNotes";
        }
    }

    /*================================
         SECTION RISQUE
    ==================================*/

    /**
     * Affiche le rapport de risque de diabète pour un patient.
     */
    @GetMapping("/patients/{id}/risk")
    public String viewRiskReport(@PathVariable("id") String patientId,
                                 Model model,
                                 HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        String url = RISK_SERVICE_URL + "/" + patientId;
        HttpHeaders headers = createHeaders(session);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        try {
            logger.info("Envoi de la requête GET vers {}", url);
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<Map>() {}
            );
            Map riskReport = response.getBody();
            model.addAttribute("riskReport", riskReport);
            return "riskReport"; // Template pour afficher le rapport de risque
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du rapport de risque pour le patient {} : {}", patientId, e.getMessage());
            model.addAttribute("error", "Erreur lors de l'évaluation du risque");
            return "redirect:/patients";
        }
    }
}