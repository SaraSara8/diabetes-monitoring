package com.diabetes.front.client;

import com.diabetes.front.dto.NoteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Client Feign pour le service Notes.
 * Fournit des méthodes pour récupérer les notes d'un patient et ajouter une nouvelle note.
 */

@FeignClient(name = "notes-service", url = "${notes.service.url:http://notes-service:8083}")
public interface NotesClient {

    @GetMapping("/api/notes/patient/{id}")
    List<NoteDto> getNotesByPatientId(@PathVariable("id") String patientId);

    @PostMapping("/api/notes")
    NoteDto addNote(@RequestBody NoteDto note);
}
