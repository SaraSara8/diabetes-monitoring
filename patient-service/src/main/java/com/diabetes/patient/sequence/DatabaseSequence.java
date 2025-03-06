package com.diabetes.patient.sequence;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Représente une séquence dans MongoDB pour générer des identifiants auto-incrémentés.
 */
@Document(collection = "database_sequences")
public class DatabaseSequence {

    @Id
    private String id; // Par exemple "patients_sequence"

    private long seq;  // Valeur actuelle du compteur

    public DatabaseSequence() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }
}
