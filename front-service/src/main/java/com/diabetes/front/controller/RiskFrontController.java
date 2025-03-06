package com.diabetes.front.controller;

import com.diabetes.front.client.RiskAssessmentClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.servlet.http.HttpSession;
import java.util.Map;

/**
 * Controller pour afficher le rapport de risque de diabète pour un patient.
 * Ce contrôleur utilise un client Feign configuré pour ajouter l'authentification Basic.
 */
@Controller
public class RiskFrontController {

    private static final Logger logger = LoggerFactory.getLogger(RiskFrontController.class);

    @Autowired
    private RiskAssessmentClient riskAssessmentClient;

    /**
     * Affiche le rapport de risque pour un patient.
     *
     * @param patientId l'identifiant du patient
     * @param model le modèle pour la vue
     * @param session la session HTTP
     * @return le nom de la vue affichant le rapport de risque, ou une redirection en cas d'erreur
     */
    @GetMapping("/patients/{id}/risk")
    public String viewRiskReport(@PathVariable("id") String patientId,
                                 Model model,
                                 HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        try {
            logger.info("Appel du RiskAssessmentClient pour le patient {}", patientId);
            Map<String, Object> riskReport = riskAssessmentClient.getRiskReport(patientId);
            model.addAttribute("riskReport", riskReport);
            return "riskReport"; // Nom du template pour afficher le rapport de risque
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du rapport de risque pour le patient {}: {}", patientId, e.getMessage());
            model.addAttribute("error", "Erreur lors de l'évaluation du risque");
            return "redirect:/patients";
        }
    }
}