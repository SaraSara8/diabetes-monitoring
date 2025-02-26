package com.diabetes.notes.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "notes")
public class Note {
    @Id
    private String id;
    private String patientId;
    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    // Constructeurs, getters et setters
}

