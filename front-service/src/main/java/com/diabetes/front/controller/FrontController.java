package com.diabetes.front.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import jakarta.servlet.http.HttpSession;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Controller principal pour le front.
 * <p>
 * Ce controller gère :
 * <ul>
 *   <li>La connexion de l'utilisateur via Basic Auth (envoie des identifiants au gateway-service).</li>
 *   <li>La déconnexion en invalidant la session.</li>
 *   <li>La création d'en-têtes HTTP pour la propagation de l'authentification.</li>
 * </ul>
 * </p>
 */
@Controller
public class FrontController {

    private static final Logger logger = LoggerFactory.getLogger(FrontController.class);

    private final RestTemplate restTemplate = new RestTemplate();

    /*================================
         SECTION LOGIN
    ==================================*/

    /**
     * Affiche le formulaire de connexion.
     *
     * @return le nom du template de connexion ("login")
     */
    @GetMapping("/login")
    public String showLoginForm() {
        logger.info("Affichage du formulaire de connexion.");
        return "login"; // Template login.html
    }

    /**
     * Traitement de la connexion de l'utilisateur via Basic Auth.
     * <p>
     * Cette méthode envoie une requête POST vers le endpoint "/auth/login" du gateway-service
     * pour valider les identifiants. Si l'authentification est réussie, les identifiants sont stockés en session.
     * </p>
     *
     * @param username le nom d'utilisateur fourni
     * @param password le mot de passe fourni
     * @param model le modèle pour transmettre des messages à la vue
     * @param session la session HTTP
     * @return une redirection vers la liste des patients en cas de succès, sinon le template "login" avec un message d'erreur
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
     * Déconnecte l'utilisateur en invalidant la session.
     *
     * @param session la session HTTP
     * @return une redirection vers la page de connexion
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        logger.info("Déconnexion de l'utilisateur.");
        session.invalidate();
        return "redirect:/login";
    }

    /**
     * Crée un en-tête HTTP contenant l'authentification Basic,
     * en récupérant les identifiants stockés dans la session.
     *
     * @param session la session HTTP
     * @return un objet HttpHeaders avec l'en-tête Authorization configuré
     */
    private HttpHeaders createHeaders(HttpSession session) {
        String username = (String) session.getAttribute("username");
        String password = (String) session.getAttribute("password");
        HttpHeaders headers = new HttpHeaders();
        if (username != null && password != null) {
            String auth = username + ":" + password;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
            headers.set("Authorization", "Basic " + encodedAuth);
            logger.debug("En-tête Basic Auth créé pour l'utilisateur: {}", username);
        }
        return headers;
    }
}