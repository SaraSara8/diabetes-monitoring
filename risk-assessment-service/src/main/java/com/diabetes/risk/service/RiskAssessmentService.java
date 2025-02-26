package com.diabetes.risk.service;

import com.diabetes.risk.dto.NoteDto;
import com.diabetes.risk.dto.PatientDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;

@Service
public class RiskAssessmentService {

    // Liste des déclencheurs
    private static final List<String> TRIGGER_TERMS = Arrays.asList(
            "Hémoglobine A1C",
            "Microalbumine",
            "Taille",
            "Poids",
            "Fumeur",
            "Fumeuse",
            "Anormal",
            "Cholestérol",
            "Vertiges",
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
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    /**
     * Compte le nombre de déclencheurs présents dans les notes.
     * La recherche est insensible à la casse.
     */
    public int countTriggers(List<NoteDto> notes) {
        int count = 0;
        for (NoteDto note : notes) {
            String content = note.getContent().toLowerCase();
            for (String term : TRIGGER_TERMS) {
                if (content.contains(term.toLowerCase())) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Évalue le risque de diabète pour un patient.
     * @param patient les infos du patient
     * @param notes la liste des notes du patient
     * @return le niveau de risque
     */
    public RiskLevel assessRisk(PatientDto patient, List<NoteDto> notes) {
        int triggerCount = countTriggers(notes);
        int age = calculateAge(patient.getDateNaissance());
        String gender = patient.getGenre().toLowerCase();

        // Aucun risque si aucune note avec déclencheur
        if (triggerCount == 0) {
            return RiskLevel.NONE;
        }

        // Cas pour patient âgé de plus de 30 ans
        if (age > 30) {
            if (triggerCount >= 2 && triggerCount <= 5) {
                return RiskLevel.BORDERLINE;
            } else if (triggerCount >= 6 && triggerCount <= 7) {
                return RiskLevel.IN_DANGER;
            } else if (triggerCount >= 8) {
                return RiskLevel.EARLY_ONSET;
            }
        } else { // patient de 30 ans ou moins
            if ("homme".equals(gender)) {
                if (triggerCount >= 3 && triggerCount < 5) {
                    return RiskLevel.IN_DANGER;
                } else if (triggerCount >= 5) {
                    return RiskLevel.EARLY_ONSET;
                }
            } else if ("femme".equals(gender)) {
                if (triggerCount >= 4 && triggerCount < 7) {
                    return RiskLevel.IN_DANGER;
                } else if (triggerCount >= 7) {
                    return RiskLevel.EARLY_ONSET;
                }
            }
        }
        // Par défaut, si aucune règle ne correspond, renvoie "None"
        return RiskLevel.NONE;
    }
}
