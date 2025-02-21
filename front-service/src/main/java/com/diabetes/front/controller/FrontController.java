package com.diabetes.front.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.ui.Model;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class FrontController {

    private static final Logger logger = LoggerFactory.getLogger(FrontController.class);
    private String jwtToken; // On stocke ça en mémoire ou en session, c’est un exemple

    @GetMapping("/login")
    public String showLoginForm() {
        logger.info("Affichage du formulaire de connexion.");
        return "login"; // login.html
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               Model model) {

        logger.info("Tentative de connexion pour l'utilisateur: {}", username);
        RestTemplate rest = new RestTemplate();
        //String url = "http://localhost:8080/auth/login";
        String url = "http://gateway-service:8080/auth/login";


        Map<String, String> creds = new HashMap<>();
        creds.put("username", username);
        creds.put("password", password);

        try {
            logger.info("Envoi de la requête POST vers {}", url);
            ResponseEntity<Map> response = rest.postForEntity(url, creds, Map.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                jwtToken = (String) response.getBody().get("token"); // Récupérer le token de la réponse
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


    @GetMapping("/patients")
    public String patients(Model model) {
        logger.info("Accès à la liste des patients.");

        if (jwtToken == null) {
            logger.warn("Aucun token JWT trouvé, redirection vers la page de login.");
            return "redirect:/login";
        }

        String url = "http://patient-service:8081/api/patients";
        RestTemplate rest = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            logger.info("Envoi de la requête GET vers {}", url);
            ResponseEntity<List> resp = rest.exchange(url, HttpMethod.GET, entity, List.class);
            List patients = resp.getBody();
            model.addAttribute("patients", patients);
            logger.info("Liste des patients récupérée avec succès.");
            return "patients"; // Afficher la liste des patients
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des patients: {}", e.getMessage());
            model.addAttribute("error", "Session expirée, veuillez vous reconnecter.");
            return "redirect:/login";
        }
    }

}
