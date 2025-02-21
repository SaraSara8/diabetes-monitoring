package com.diabetes.patient.service;

import com.diabetes.patient.model.Patient;
import com.diabetes.patient.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Service pour gérer les opérations liées aux patients.
 */
@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    /**
     * Récupère la liste de tous les patients.
     * @return liste des patients
     */
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    /**
     * Récupère un patient par son identifiant.
     * @param id identifiant du patient
     * @return Optional contenant le patient s'il existe, sinon vide
     */
    public Optional<Patient> getPatientById(String id) {
        return patientRepository.findById(id);
    }

    /**
     * Crée un nouveau patient.
     * @param patient objet Patient à créer
     * @return le patient créé
     */
    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    /**
     * Met à jour les informations d'un patient existant.
     * @param id identifiant du patient à mettre à jour
     * @param updatedPatient objet contenant les informations mises à jour
     * @return le patient mis à jour, ou null s'il n'existe pas
     */
    public Patient updatePatient(String id, Patient updatedPatient) {
        return patientRepository.findById(id)
                .map(existingPatient -> {
                    existingPatient.setPrenom(updatedPatient.getPrenom());
                    existingPatient.setNom(updatedPatient.getNom());
                    existingPatient.setDateNaissance(updatedPatient.getDateNaissance());
                    existingPatient.setGenre(updatedPatient.getGenre());
                    existingPatient.setAdressePostale(updatedPatient.getAdressePostale());
                    existingPatient.setNumeroTelephone(updatedPatient.getNumeroTelephone());
                    return patientRepository.save(existingPatient);
                })
                .orElse(null);
    }
}
