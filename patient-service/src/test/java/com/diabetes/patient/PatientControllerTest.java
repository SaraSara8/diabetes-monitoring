package com.diabetes.patient;

import com.diabetes.patient.model.Patient;
import com.diabetes.patient.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PatientControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PatientRepository patientRepository;

    @BeforeEach
    public void setup() {
        // Nettoie la base de données avant chaque test
        patientRepository.deleteAll();
    }

    @Test
    public void testCreatePatients() {
        // Cas de test 1 : TestNone
        Patient p1 = new Patient();
        p1.setNom("TestNone");
        p1.setPrenom("Test");
        p1.setDateNaissance(LocalDate.of(1966, 12, 31));
        p1.setGenre("F");
        p1.setAdressePostale("1 Brookside St");
        p1.setNumeroTelephone("100-222-3333");

        // Cas de test 2 : TestBorderline
        Patient p2 = new Patient();
        p2.setNom("TestBorderline");
        p2.setPrenom("Test");
        p2.setDateNaissance(LocalDate.of(1945, 6, 24));
        p2.setGenre("M");
        p2.setAdressePostale("2 High St");
        p2.setNumeroTelephone("200-333-4444");

        // Cas de test 3 : TestInDanger
        Patient p3 = new Patient();
        p3.setNom("TestInDanger");
        p3.setPrenom("Test");
        p3.setDateNaissance(LocalDate.of(2004, 6, 18));
        p3.setGenre("M");
        p3.setAdressePostale("3 Club Road");
        p3.setNumeroTelephone("300-444-5555");

        // Cas de test 4 : TestEarlyOnset
        Patient p4 = new Patient();
        p4.setNom("TestEarlyOnset");
        p4.setPrenom("Test");
        p4.setDateNaissance(LocalDate.of(2002, 6, 28));
        p4.setGenre("F");
        p4.setAdressePostale("4 Valley Dr");
        p4.setNumeroTelephone("400-555-6666");

        // Effectuer les appels POST pour créer les patients
        ResponseEntity<Patient> response1 = restTemplate.postForEntity("/api/patients", p1, Patient.class);
        ResponseEntity<Patient> response2 = restTemplate.postForEntity("/api/patients", p2, Patient.class);
        ResponseEntity<Patient> response3 = restTemplate.postForEntity("/api/patients", p3, Patient.class);
        ResponseEntity<Patient> response4 = restTemplate.postForEntity("/api/patients", p4, Patient.class);

        // Vérifier que la création a réussi (HTTP 200 OK) et que les IDs ne sont pas null
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response1.getBody().getId()).isNotNull();
        assertThat(response2.getBody().getId()).isNotNull();
        assertThat(response3.getBody().getId()).isNotNull();
        assertThat(response4.getBody().getId()).isNotNull();
    }

    @Test
    public void testGetPatientById() {
        // Créer un patient pour le test
        Patient p = new Patient();
        p.setNom("TestGet");
        p.setPrenom("Test");
        p.setDateNaissance(LocalDate.of(1970, 1, 1));
        p.setGenre("M");
        p.setAdressePostale("123 Main St");
        p.setNumeroTelephone("123-456-7890");

        Patient savedPatient = patientRepository.save(p);

        // Récupérer le patient via l'API
        ResponseEntity<Patient> response = restTemplate.getForEntity("/api/patients/" + savedPatient.getId(), Patient.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Patient retrievedPatient = response.getBody();
        assertThat(retrievedPatient).isNotNull();
        assertThat(retrievedPatient.getNom()).isEqualTo("TestGet");
    }
}
