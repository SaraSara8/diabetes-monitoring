package com.diabetes.risk;

import com.diabetes.risk.service.RiskAssessmentService;
import com.diabetes.risk.dto.NoteDto;
import com.diabetes.risk.dto.PatientDto;
import com.diabetes.risk.service.RiskAssessmentService.RiskLevel;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RiskAssessmentServiceTest {

    private final RiskAssessmentService service = new RiskAssessmentService();

    /**
     * Crée une note contenant le déclencheur donné.
     */
    private NoteDto createNote(String content) {
        NoteDto note = new NoteDto();
        note.setContent(content);
        return note;
    }

    /**
     * Prépare un patient avec la date de naissance permettant de simuler l'âge souhaité.
     * Si age est > 30, on retourne un patient de 40 ans par exemple.
     */
    private PatientDto createPatient(String id, String prenom, String nom, int age, String gender) {
        PatientDto patient = new PatientDto();
        patient.setId(id);
        patient.setPrenom(prenom);
        patient.setNom(nom);
        patient.setGenre(gender);
        // Calcule la date de naissance pour obtenir l'âge souhaité
        patient.setDateNaissance(LocalDate.now().minusYears(age));
        return patient;
    }

    // ---------------------------
    // Pour les patients de plus de 30 ans
    // ---------------------------

    @Test
    public void testAssessRiskOver30_Borderline() {
        // Patient de 40 ans, unique trigger count entre 2 et 5 (ici 3 déclencheurs)
        PatientDto patient = createPatient("1", "John", "Doe", 40, "M");
        List<NoteDto> notes = Arrays.asList(
                createNote("Hémoglobine A1C"),  // déclencheur 1
                createNote("Cholestérol"),       // déclencheur 2
                createNote("Vertiges")           // déclencheur 3
        );
        RiskLevel level = service.assessRisk(patient, notes);
        assertEquals(RiskLevel.BORDERLINE, level);
    }

    @Test
    public void testAssessRiskOver30_InDanger() {
        // Patient de 45 ans, 6 déclencheurs uniques
        PatientDto patient = createPatient("2", "Alice", "Smith", 45, "F");
        List<NoteDto> notes = Arrays.asList(
                createNote("Hémoglobine A1C"),   // 1
                createNote("Cholestérol"),        // 2
                createNote("Microalbumine"),      // 3
                createNote("Vertiges"),           // 4
                createNote("Fumeur"),             // 5
                createNote("Rechute")             // 6
        );
        RiskLevel level = service.assessRisk(patient, notes);
        assertEquals(RiskLevel.IN_DANGER, level);
    }

    @Test
    public void testAssessRiskOver30_EarlyOnset() {
        // Patient de 50 ans, 8 déclencheurs uniques
        PatientDto patient = createPatient("3", "Bob", "Johnson", 50, "M");
        List<NoteDto> notes = Arrays.asList(
                createNote("Hémoglobine A1C"),   // 1
                createNote("Cholestérol"),        // 2
                createNote("Microalbumine"),      // 3
                createNote("Vertiges"),           // 4
                createNote("Fumeur"),             // 5
                createNote("Rechute"),            // 6
                createNote("Anticorps"),          // 7
                createNote("Taille")              // 8
        );
        RiskLevel level = service.assessRisk(patient, notes);
        assertEquals(RiskLevel.EARLY_ONSET, level);
    }

    // ---------------------------
    // Pour les patients de 30 ans ou moins (hommes)
    // ---------------------------

    @Test
    public void testAssessRiskUnder30Male_InDanger() {
        // Homme de 25 ans, 3 déclencheurs (entre 3 et 4)
        PatientDto patient = createPatient("4", "Charlie", "Brown", 25, "M");
        List<NoteDto> notes = Arrays.asList(
                createNote("Fumeur"),         // 1
                createNote("Vertiges"),        // 2
                createNote("Hémoglobine A1C")   // 3
        );
        RiskLevel level = service.assessRisk(patient, notes);
        assertEquals(RiskLevel.IN_DANGER, level);
    }

    @Test
    public void testAssessRiskUnder30Male_EarlyOnset() {
        // Homme de 28 ans, 5 déclencheurs ou plus
        PatientDto patient = createPatient("5", "David", "Miller", 28, "M");
        List<NoteDto> notes = Arrays.asList(
                createNote("Fumeur"),         // 1
                createNote("Vertiges"),        // 2
                createNote("Hémoglobine A1C"),   // 3
                createNote("Cholestérol"),       // 4
                createNote("Microalbumine")      // 5
        );
        RiskLevel level = service.assessRisk(patient, notes);
        assertEquals(RiskLevel.EARLY_ONSET, level);
    }

    // ---------------------------
    // Pour les patients de 30 ans ou moins (femmes)
    // ---------------------------

    @Test
    public void testAssessRiskUnder30Female_InDanger() {
        // Femme de 29 ans, 4 déclencheurs (entre 4 et 6)
        PatientDto patient = createPatient("6", "Eve", "Wilson", 29, "F");
        List<NoteDto> notes = Arrays.asList(
                createNote("Hémoglobine A1C"),   // 1
                createNote("Cholestérol"),        // 2
                createNote("Vertiges"),           // 3
                createNote("Fumeuse")             // 4 (en supposant que "Fumeuse" déclenche)
        );
        RiskLevel level = service.assessRisk(patient, notes);
        assertEquals(RiskLevel.IN_DANGER, level);
    }

    @Test
    public void testAssessRiskUnder30Female_EarlyOnset() {
        // Femme de 27 ans, 7 déclencheurs ou plus
        PatientDto patient = createPatient("7", "Fiona", "Garcia", 27, "F");
        List<NoteDto> notes = Arrays.asList(
                createNote("Hémoglobine A1C"),   // 1
                createNote("Cholestérol"),        // 2
                createNote("Microalbumine"),      // 3
                createNote("Vertiges"),           // 4
                createNote("Fumeuse"),            // 5
                createNote("Anticorps"),          // 6
                createNote("Rechute")             // 7
        );
        RiskLevel level = service.assessRisk(patient, notes);
        assertEquals(RiskLevel.EARLY_ONSET, level);
    }

    // ---------------------------
    // Cas par défaut : aucun déclencheur trouvé
    // ---------------------------

    @Test
    public void testAssessRisk_NoTriggers() {
        PatientDto patient = createPatient("8", "George", "King", 35, "M");
        List<NoteDto> notes = Arrays.asList(
                createNote("Pas de déclencheur ici"),
                createNote("Aucune information pertinente")
        );
        RiskLevel level = service.assessRisk(patient, notes);
        assertEquals(RiskLevel.NONE, level);
    }
}
