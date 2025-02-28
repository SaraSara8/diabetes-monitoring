package com.diabetes.risk.client;

import com.diabetes.risk.dto.NoteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.diabetes.risk.config.NotesFeignConfig;
import java.util.List;

/**
 * Client Feign pour communiquer avec le microservice notes.
 */
@FeignClient(name = "notes-service", url = "${notes.service.url:http://notes-service:8083/api/notes}", configuration = NotesFeignConfig.class)
public interface NotesClient {

    /**
     * Récupère la liste des notes pour un patient donné.
     *
     * @param patientId l'identifiant du patient
     * @return la réponse contenant la liste des notes
     */
    @GetMapping("/patient/{patientId}")
    ResponseEntity<List<NoteDto>> getNotesByPatientId(@PathVariable("patientId") String patientId);
}