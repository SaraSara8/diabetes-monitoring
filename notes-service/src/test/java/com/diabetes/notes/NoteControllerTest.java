package com.diabetes.notes;

import com.diabetes.notes.Dto.PatientDto;
import com.diabetes.notes.model.Note;
import com.diabetes.notes.repository.NoteRepository;
import com.diabetes.notes.client.PatientClient;
import feign.FeignException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NoteControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private NoteRepository noteRepository;

    // Utiliser @MockBean pour remplacer PatientClient dans le contexte Spring de test
    @MockitoBean
    private PatientClient patientClient;

    @AfterEach
    public void cleanup() {
        // Supprimez toutes les notes créées durant les tests
        noteRepository.deleteAll();
    }

    @Test
    public void testAddNote_PatientNotFound() {
        String patientId = "nonexistent";

        // Configurer le mock pour simuler l'absence de patient via une exception NotFound
        when(patientClient.getPatientById(patientId)).thenThrow(FeignException.NotFound.class);

        Note note = new Note();
        note.setPatientId(patientId);
        note.setContent("Test note - Patient not found");

        ResponseEntity<String> response = restTemplate
                .withBasicAuth("admin", "1234")
                .postForEntity("/api/notes", note, String.class);

        // On attend un code 404 NOT_FOUND lorsque le patient n'existe pas
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAddNote_Success() {
        String patientId = "1111";
        PatientDto patientDto = new PatientDto();
        patientDto.setId(patientId);
        patientDto.setNom("Doe");
        patientDto.setPrenom("John");

        // Configurer le mock pour retourner un PatientDto valide
        when(patientClient.getPatientById(patientId)).thenReturn(ResponseEntity.ok(patientDto));

        Note note = new Note();
        note.setPatientId(patientId);
        note.setContent("Test note - Success");

        ResponseEntity<Note> response = restTemplate
                .withBasicAuth("admin", "1234")
                .postForEntity("/api/notes", note, Note.class);

        // Vérifier qu'un code 200 OK est retourné et qu'une note avec un ID est renvoyée
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Note savedNote = response.getBody();
        assertThat(savedNote).isNotNull();
        assertThat(savedNote.getId()).isNotNull();
    }

    @Test
    public void testGetNotesByPatientId() {
        // Insérer une note directement dans la base via le repository
        Note note = new Note();
        note.setPatientId("1");
        note.setContent("Test note for getNotes");
        note.setCreatedAt(LocalDateTime.now());
        noteRepository.save(note);

        ResponseEntity<Note[]> response = restTemplate
                .withBasicAuth("admin", "1234")
                .getForEntity("/api/notes/patient/1", Note[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Note[] notes = response.getBody();
        assertThat(notes).isNotNull();
        assertThat(notes.length).isGreaterThan(0);
    }
}
