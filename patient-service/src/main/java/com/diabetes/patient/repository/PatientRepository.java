package com.diabetes.patient.repository;


import com.diabetes.patient.model.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository pour la gestion des patients dans MongoDB.
 */
@Repository
public interface PatientRepository extends MongoRepository<Patient, String> {
    // Ajoutez ici des méthodes de requête personnalisées si nécessaire
}