package com.diabetes.risk;

import com.diabetes.risk.dto.NoteDto;
import com.diabetes.risk.dto.PatientDto;
import com.diabetes.risk.client.PatientClient;
import com.diabetes.risk.client.NotesClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RiskAssessmentControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockitoBean
    private PatientClient patientClient;

    @MockitoBean
    private NotesClient notesClient;

    @Test
    public void testGetRiskReport_PatientNotFound() {
        String patientId = "nonexistent";

        // Simuler une réponse 404 du client patient
        when(patientClient.getPatientById(patientId))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));

        ResponseEntity<String> response = restTemplate
                .withBasicAuth("admin", "1234")
                .getForEntity("/api/risk/" + patientId, String.class);

        // On attend une réponse 404
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetRiskReport_Success() {
        String patientId = "1111";

        // Préparer un PatientDto fictif
        PatientDto patientDto = new PatientDto();
        patientDto.setId(patientId);
        patientDto.setNom("Doe");
        patientDto.setPrenom("John");
        patientDto.setDateNaissance(LocalDate.of(1980, 1, 1));
        patientDto.setGenre("M");

        // Préparer deux notes fictives
        NoteDto note1 = new NoteDto();
        note1.setId("n1");
        note1.setPatientId(patientId);
        note1.setContent("Hémoglobine A1C test");
        note1.setCreatedAt(LocalDate.now().atStartOfDay());

        NoteDto note2 = new NoteDto();
        note2.setId("n2");
        note2.setPatientId(patientId);
        note2.setContent("Vertiges et fumeur");
        note2.setCreatedAt(LocalDate.now().atStartOfDay());

        List<NoteDto> notes = Arrays.asList(note1, note2);

        // Configurer les mocks pour retourner des réponses valides
        when(patientClient.getPatientById(patientId))
                .thenReturn(ResponseEntity.ok(patientDto));
        when(notesClient.getNotesByPatientId(patientId))
                .thenReturn(ResponseEntity.ok(notes));

        ResponseEntity<Map> response = restTemplate
                .withBasicAuth("admin", "1234")
                .getForEntity("/api/risk/" + patientId, Map.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> report = response.getBody();
        assertNotNull(report);
        // Vérifier la présence des clés attendues dans le rapport
        assertTrue(report.containsKey("patientId"));
        assertTrue(report.containsKey("patientPrenom"));
        assertTrue(report.containsKey("patientNom"));
        assertTrue(report.containsKey("riskLevel"));
        assertTrue(report.containsKey("triggerCount"));
        assertTrue(report.containsKey("patient"));
        assertTrue(report.containsKey("notes"));
    }
}
