package com.diabetes.notes.repository;

import com.diabetes.notes.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;


/**
 * Repository pour l'entité {@link com.diabetes.notes.model.Note}.
 *
 * <p>
 * Cette interface étend {@link org.springframework.data.mongodb.repository.MongoRepository} pour fournir
 * des opérations CRUD de base sur les documents Note dans MongoDB.
 * </p>
 *
 * <p>
 * Elle inclut également une méthode personnalisée permettant de récupérer toutes les notes associées
 * à un identifiant de patient spécifique.
 * </p>
 */


public interface NoteRepository extends MongoRepository<Note, String> {
    List<Note> findByPatientId(String patientId);
}

