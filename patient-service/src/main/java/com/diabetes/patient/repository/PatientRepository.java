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
     * Recherche paginée des patients dont le nom ou le prénom contient la chaîne donnée, sans tenir compte de la casse.
     *
     * @param nom la chaîne à rechercher dans le nom du patient
     * @param prenom la chaîne à rechercher dans le prénom du patient
     * @param pageable l'objet Pageable définissant la pagination
     * @return une page de patients correspondant aux critères de recherche
     */
    Page<Patient> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom, Pageable pageable);


    /**
     * Recherche un patient par une correspondance exacte sur le nom, le prénom et la date de naissance.
     *
     * @param nom le nom du patient
     * @param prenom le prénom du patient
     * @param dateNaissance la date de naissance du patient
     * @return un Optional contenant le patient trouvé, ou vide s'il n'existe pas
     */
    Optional<Patient> findByNomAndPrenomAndDateNaissance(String nom, String prenom, LocalDate dateNaissance);
}
