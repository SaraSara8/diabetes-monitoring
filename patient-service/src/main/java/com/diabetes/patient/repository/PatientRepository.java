package com.diabetes.patient.repository;

import com.diabetes.patient.model.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Repository pour l'entité Patient.
 */
public interface PatientRepository extends MongoRepository<Patient, String> {
    /**
     * Recherche paginée par nom ou prénom (ignorant la casse).
     */
    Page<Patient> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom, Pageable pageable);

    Optional<Patient> findByNomAndPrenomAndDateNaissance(String nom, String prenom, LocalDate dateNaissance);


}
