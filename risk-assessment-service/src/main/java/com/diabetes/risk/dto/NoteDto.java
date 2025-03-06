package com.diabetes.risk.dto;

import java.time.LocalDateTime;


/**
 * DTO représentant une note, incluant son identifiant, l'identifiant du patient associé, le contenu et la date de création.
 */

public class NoteDto {
    private String id;
    private String patientId;
    private String content;
    private LocalDateTime createdAt;

    // Getters et setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPatientId() {
        return patientId;
    }
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

