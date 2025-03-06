package com.diabetes.patient.controller;

import com.diabetes.patient.model.Patient;
import com.diabetes.patient.dto.PageDto;
import com.diabetes.patient.repository.PatientRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
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
        // Optionnel : initialiser ou vider des données si nécessaire.
    }

    @AfterEach
    public void cleanup() {
        // Supprimez uniquement les enregistrements créés pendant le test.
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

    @Test
    public void testGetPatientByNonExistentId() {
        // Tentative de récupération d'un patient inexistant
        ResponseEntity<Patient> response = restTemplate
                .withBasicAuth("admin", "1234")
                .getForEntity("/api/patients/nonexistent-id", Patient.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testUpdatePatient() {
        // Créer un patient initial
        Patient p = new Patient();
        p.setNom("Original");
        p.setPrenom("Patient");
        p.setDateNaissance(LocalDate.of(1980, 1, 1));
        p.setGenre("M");
        p.setAdressePostale("Address 1");
        p.setNumeroTelephone("000-000-0000");
        Patient savedPatient = patientRepository.save(p);
        testPatientIds.add(savedPatient.getId());

        // Préparer un patient mis à jour
        Patient updated = new Patient();
        updated.setNom("Updated");
        updated.setPrenom("PatientUpdated");
        updated.setDateNaissance(LocalDate.of(1985, 5, 5));
        updated.setGenre("F");
        updated.setAdressePostale("Address Updated");
        updated.setNumeroTelephone("111-111-1111");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Patient> updateRequest = new HttpEntity<>(updated, headers);

        ResponseEntity<Patient> updateResponse = restTemplate
                .withBasicAuth("admin", "1234")
                .exchange("/api/patients/" + savedPatient.getId(), HttpMethod.PUT, updateRequest, Patient.class);

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Patient updatedPatient = updateResponse.getBody();
        assertThat(updatedPatient).isNotNull();
        assertThat(updatedPatient.getNom()).isEqualTo("Updated");
        assertThat(updatedPatient.getPrenom()).isEqualTo("PatientUpdated");
        assertThat(updatedPatient.getAdressePostale()).isEqualTo("Address Updated");
    }

    @Test
    public void testGetPatients() {
        // Créer plusieurs patients pour tester la pagination
        Patient p1 = new Patient("Nom1", "Prenom1", LocalDate.of(1990, 1, 1), "M", "Address1", "111-111-1111");
        Patient p2 = new Patient("Nom2", "Prenom2", LocalDate.of(1992, 2, 2), "F", "Address2", "222-222-2222");

        Patient savedP1 = patientRepository.save(p1);
        Patient savedP2 = patientRepository.save(p2);
        testPatientIds.add(savedP1.getId());
        testPatientIds.add(savedP2.getId());

        // Utilisation d'un ParameterizedTypeReference pour récupérer un objet PageDto<Patient>
        ParameterizedTypeReference<PageDto<Patient>> responseType = new ParameterizedTypeReference<PageDto<Patient>>() {};

        ResponseEntity<PageDto<Patient>> response = restTemplate
                .withBasicAuth("admin", "1234")
                .exchange("/api/patients?page=0&size=10", HttpMethod.GET, null, responseType);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        PageDto<Patient> pageDto = response.getBody();
        assertThat(pageDto).isNotNull();
        // On s'attend à trouver au moins nos 2 patients
        assertThat(pageDto.getContent()).hasSizeGreaterThanOrEqualTo(2);
    }
}
