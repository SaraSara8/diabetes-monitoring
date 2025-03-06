package com.diabetes.patient;

import com.diabetes.patient.model.Patient;
import com.diabetes.patient.repository.PatientRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PatientControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PatientRepository patientRepository;

    // Conserver les IDs des patients créés pendant les tests
    private List<String> testPatientIds = new ArrayList<>();

    @BeforeEach
    public void setup() {
        // Optionnel : vous pouvez ne rien supprimer ici afin de ne pas toucher aux vraies données.
    }

    @AfterEach
    public void cleanup() {
        // Supprimez uniquement les enregistrements créés pendant le test
        for (String id : testPatientIds) {
            patientRepository.deleteById(id);
        }
        testPatientIds.clear();
    }

    @Test
    public void testCreatePatients() {
        Patient p1 = new Patient();
        p1.setNom("TOTOTest");
        p1.setPrenom("Test");
        p1.setDateNaissance(LocalDate.of(1966, 12, 31));
        p1.setGenre("F");
        p1.setAdressePostale("1 Brookside St");
        p1.setNumeroTelephone("100-222-3333");

        ResponseEntity<Patient> response1 = restTemplate
                .withBasicAuth("admin", "1234")
                .postForEntity("/api/patients", p1, Patient.class);

        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);
        String createdId = response1.getBody().getId();
        assertThat(createdId).isNotNull();
        testPatientIds.add(createdId);
    }

    @Test
    public void testGetPatientById() {
        Patient p = new Patient();
        p.setNom("TestGet");
        p.setPrenom("Test");
        p.setDateNaissance(LocalDate.of(1970, 1, 1));
        p.setGenre("M");
        p.setAdressePostale("123 Main St");
        p.setNumeroTelephone("123-456-7890");

        Patient savedPatient = patientRepository.save(p);
        testPatientIds.add(savedPatient.getId());

        ResponseEntity<Patient> response = restTemplate
                .withBasicAuth("admin", "1234")
                .getForEntity("/api/patients/" + savedPatient.getId(), Patient.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Patient retrievedPatient = response.getBody();
        assertThat(retrievedPatient).isNotNull();
        assertThat(retrievedPatient.getNom()).isEqualTo("TestGet");
    }
}
