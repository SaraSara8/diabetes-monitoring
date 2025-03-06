package com.diabetes.notes.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;



/**
 * Représente une note associée à un patient.
 *
 * <p>Cette classe définit une entité Note qui est stockée dans la collection "notes" de MongoDB.
 * Elle contient un identifiant unique, l'identifiant du patient auquel la note se rapporte, le contenu textuel
 * de la note, ainsi que la date de création automatiquement renseignée lors de l'insertion.</p>
 */

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

