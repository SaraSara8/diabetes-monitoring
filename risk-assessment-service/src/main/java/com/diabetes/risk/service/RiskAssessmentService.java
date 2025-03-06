package com.diabetes.risk.service;

import com.diabetes.risk.dto.NoteDto;
import com.diabetes.risk.dto.PatientDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class RiskAssessmentService {

    private static final Logger logger = LoggerFactory.getLogger(RiskAssessmentService.class);

    // Liste des déclencheurs
    private static final List<String> TRIGGER_TERMS = Arrays.asList(
            "Hémoglobine A1C",
            "Microalbumine",
            "Taille",
            "Poids",
            "Fumeur",
            "Fumeuse",
            "Anormal",
            "Anormale",
            "Anormales",
            "Cholestérol",
            "Vertiges",
            "Vertige",
            "Rechute",
            "Réaction",
            "Anticorps"
    );

    public enum RiskLevel {
        NONE, BORDERLINE, IN_DANGER, EARLY_ONSET
    }

    /**
     * Calcule l'âge du patient en années.
     */
    private int calculateAge(LocalDate birthDate) {
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        logger.debug("Âge calculé: {}", age);
        return age;
    }

    /**
     * Compte le nombre de déclencheurs uniques présents dans les notes.
     * Même si un déclencheur apparaît plusieurs fois (dans la même note ou dans plusieurs notes),
     * il est compté une seule fois.
     *
     * @param notes la liste des notes du patient
     * @return le nombre de déclencheurs uniques trouvés
     */
    public int countUniqueTriggers(List<NoteDto> notes) {
        Set<String> uniqueTriggers = new HashSet<>();
        logger.debug("Début du comptage des déclencheurs uniques dans {} note(s).", notes.size());
        // Parcours de toutes les notes
        for (NoteDto note : notes) {
            String content = note.getContent().toLowerCase();
            logger.debug("Analyse de la note: {}", content);
            // Pour chaque terme déclencheur
            for (String term : TRIGGER_TERMS) {
                String lowerTerm = term.toLowerCase();
                // Utilisation d'une expression régulière pour trouver le terme comme un mot entier
                String regex = "\\b" + Pattern.quote(lowerTerm) + "\\b";
                if (Pattern.compile(regex).matcher(content).find()) {
                    if (uniqueTriggers.add(lowerTerm)) {
                        logger.debug("Déclencheur trouvé et ajouté: {}", lowerTerm);
                    } else {
                        logger.debug("Déclencheur déjà présent: {}", lowerTerm);
                    }
                }
            }
        }
        logger.debug("Nombre total de déclencheurs uniques trouvés: {}", uniqueTriggers.size());
        return uniqueTriggers.size();
    }

    /**
     * Évalue le risque de diabète pour un patient selon les règles suivantes :
     *
     * - Aucun risque (None) : Aucun déclencheur n'est trouvé.
     * - Risque limité (Borderline) : Pour un patient de plus de 30 ans, entre 2 et 5 déclencheurs uniques.
     * - Danger (In Danger) :
     *     - Pour un patient de plus de 30 ans : 6 ou 7 déclencheurs uniques.
     *     - Pour un homme de 30 ans ou moins : au moins 3 déclencheurs uniques, mais moins de 5.
     *     - Pour une femme de 30 ans ou moins : 4 déclencheurs uniques ou plus, mais moins de 7.
     * - Apparition précoce (Early onset) :
     *     - Pour un patient de plus de 30 ans : 8 déclencheurs uniques ou plus.
     *     - Pour un homme de 30 ans ou moins : au moins 5 déclencheurs uniques.
     *     - Pour une femme de 30 ans ou moins : au moins 7 déclencheurs uniques.
     *
     * @param patient les informations du patient
     * @param notes la liste des notes du patient
     * @return le niveau de risque
     */
    public RiskLevel assessRisk(PatientDto patient, List<NoteDto> notes) {
        int uniqueTriggerCount = countUniqueTriggers(notes);
        int age = calculateAge(patient.getDateNaissance());
        String gender = patient.getGenre().toLowerCase();

        logger.info("Évaluation du risque pour le patient ID: {} - Âge: {}, Genre: {}, Déclencheurs uniques: {}", patient.getId(), age, gender, uniqueTriggerCount);

        // Aucun risque si aucun déclencheur n'est trouvé
        if (uniqueTriggerCount == 0) {
            logger.info("Aucun déclencheur trouvé, risque: NONE");
            return RiskLevel.NONE;
        }

        // Pour les patients de plus de 30 ans
        if (age > 30) {
            if (uniqueTriggerCount >= 2 && uniqueTriggerCount <= 5) {
                logger.info("Patient de plus de 30 ans avec déclencheurs entre 2 et 5, risque: BORDERLINE");
                return RiskLevel.BORDERLINE;
            } else if (uniqueTriggerCount >= 6 && uniqueTriggerCount <= 7) {
                logger.info("Patient de plus de 30 ans avec 6 ou 7 déclencheurs, risque: IN_DANGER");
                return RiskLevel.IN_DANGER;
            } else if (uniqueTriggerCount >= 8) {
                logger.info("Patient de plus de 30 ans avec 8 déclencheurs ou plus, risque: EARLY_ONSET");
                return RiskLevel.EARLY_ONSET;
            }
        } else { // Pour les patients de 30 ans ou moins
            if ("m".equals(gender)) {
                if (uniqueTriggerCount >= 3 && uniqueTriggerCount < 5) {
                    logger.info("Homme de 30 ans ou moins avec 3 à 5 déclencheurs, risque: IN_DANGER");
                    return RiskLevel.IN_DANGER;
                } else if (uniqueTriggerCount >= 5) {
                    logger.info("Homme de 30 ans ou moins avec au moins 5 déclencheurs, risque: EARLY_ONSET");
                    return RiskLevel.EARLY_ONSET;
                }
            } else if ("f".equals(gender)) {
                if (uniqueTriggerCount >= 4 && uniqueTriggerCount < 7) {
                    logger.info("Femme de 30 ans ou moins avec 4 à 7 déclencheurs, risque: IN_DANGER");
                    return RiskLevel.IN_DANGER;
                } else if (uniqueTriggerCount >= 7) {
                    logger.info("Femme de 30 ans ou moins avec au moins 7 déclencheurs, risque: EARLY_ONSET");
                    return RiskLevel.EARLY_ONSET;
                }
            }
        }
        logger.info("Aucune règle applicable, risque par défaut: NONE");
        // Par défaut, si aucune règle ne correspond, renvoie "None"
        return RiskLevel.NONE;
    }
}
