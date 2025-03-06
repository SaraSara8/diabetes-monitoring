package com.diabetes.patient.service;

import com.diabetes.patient.model.Patient;
import com.diabetes.patient.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Service pour gérer les opérations liées aux patients.
 */
@Service
public class PatientService {

    private static final Logger logger = LoggerFactory.getLogger(PatientService.class);

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    /**
     * Récupère la liste de tous les patients.
     *
     * @return une liste de patients
     */
    public List<Patient> getAllPatients() {
        logger.debug("Récupération de tous les patients");
        return patientRepository.findAll();
    }

    /**
     * Récupère un patient par son identifiant.
     *
     * @param id identifiant du patient
     * @return un Optional contenant le patient s'il existe, sinon vide
     */
    public Optional<Patient> getPatientById(String id) {
        logger.debug("Récupération du patient avec l'id : {}", id);
        return patientRepository.findById(id);
    }

    /**
     * Crée un nouveau patient en générant un identifiant auto-incrémenté s'il n'est pas déjà renseigné.
     *
     * @param patient l'objet Patient à créer
     * @return le patient créé
     */
    public Patient createPatient(Patient patient) {
        if (patient.getId() == null) {
            long seq = sequenceGeneratorService.generateSequence("patients_sequence");
            patient.setId(String.valueOf(seq));
            logger.debug("ID auto-incrémenté généré pour le patient : {}", patient.getId());
        }
        Patient saved = patientRepository.save(patient);
        logger.info("Patient créé avec succès, id : {}", saved.getId());
        return saved;
    }

    /**
     * Met à jour les informations d'un patient existant.
     *
     * @param id             l'identifiant du patient à mettre à jour
     * @param updatedPatient l'objet Patient contenant les informations mises à jour
     * @return le patient mis à jour ou null s'il n'existe pas
     */
    public Patient updatePatient(String id, Patient updatedPatient) {
        logger.debug("Mise à jour du patient avec l'id : {}", id);
        return patientRepository.findById(id)
                .map(existingPatient -> {
                    existingPatient.setPrenom(updatedPatient.getPrenom());
                    existingPatient.setNom(updatedPatient.getNom());
                    existingPatient.setDateNaissance(updatedPatient.getDateNaissance());
                    existingPatient.setGenre(updatedPatient.getGenre());
                    existingPatient.setAdressePostale(updatedPatient.getAdressePostale());
                    existingPatient.setNumeroTelephone(updatedPatient.getNumeroTelephone());
                    Patient saved = patientRepository.save(existingPatient);
                    logger.info("Patient mis à jour, id : {}", saved.getId());
                    return saved;
                })
                .orElseGet(() -> {
                    logger.warn("Patient non trouvé pour l'id : {}", id);
                    return null;
                });
    }
}
