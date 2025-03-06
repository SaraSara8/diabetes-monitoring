package com.diabetes.patient.controller;

import com.diabetes.patient.model.Patient;
import com.diabetes.patient.dto.PageDto;
import com.diabetes.patient.repository.PatientRepository;
import com.diabetes.patient.service.PatientService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


/**
 * Contrôleur REST pour la gestion des patients.
 *
 * <p>
 * Fournit des endpoints pour récupérer une liste paginée de patients, créer un nouveau patient
 * (avec génération d'identifiant auto-incrémenté via le PatientService), récupérer un patient par son identifiant,
 * et mettre à jour les informations d'un patient existant.
 * </p>
 */

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private static final Logger logger = LogManager.getLogger(PatientController.class);

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientService patientService;

    /**
     * Récupère une page de patients.
     * Si un paramètre "query" est fourni, effectue une recherche par nom ou prénom (sans tenir compte de la classe).
     */
    @GetMapping
    public ResponseEntity<PageDto<Patient>> getPatients(
            @RequestParam(value = "query", required = false) String query,
            Pageable pageable) {
        logger.debug("Récupération des patients avec query : {}", query);
        Page<Patient> patients;
        if (query != null && !query.trim().isEmpty()) {
            patients = patientRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(query, query, pageable);
        } else {
            patients = patientRepository.findAll(pageable);
        }

        PageDto<Patient> pageDto = new PageDto<>();
        pageDto.setContent(patients.getContent());
        pageDto.setTotalElements(patients.getTotalElements());
        pageDto.setTotalPages(patients.getTotalPages());
        pageDto.setNumber(patients.getNumber());
        pageDto.setSize(patients.getSize());
        pageDto.setFirst(patients.isFirst());
        pageDto.setLast(patients.isLast());

        return ResponseEntity.ok(pageDto);
    }

    /**
     * Crée un nouveau patient. Utilise le PatientService pour générer un identifiant auto-incrémenté si nécessaire.
     */
    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        logger.debug("Création d'un patient : {} {}", patient.getPrenom(), patient.getNom());
        // Vérifie si le patient existe déjà
        if (patientRepository.findByNomAndPrenomAndDateNaissance(patient.getNom(), patient.getPrenom(), patient.getDateNaissance()).isEmpty()) {
            Patient savedPatient = patientService.createPatient(patient);
            logger.info("Patient créé avec succès, id : {}", savedPatient.getId());
            return ResponseEntity.ok(savedPatient);
        } else {
            logger.error("Le patient existe déjà : {} {}", patient.getPrenom(), patient.getNom());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Récupère un patient par son identifiant.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable String id) {
        logger.debug("Récupération du patient avec l'id : {}", id);
        Optional<Patient> patient = patientRepository.findById(id);
        return patient.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("Patient non trouvé pour l'id : {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    /**
     * Met à jour les informations d'un patient existant.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable String id, @RequestBody Patient updatedPatient) {
        logger.debug("Mise à jour du patient avec l'id : {}", id);
        Optional<Patient> existing = patientRepository.findById(id);
        if (existing.isPresent()) {
            Patient patient = existing.get();
            patient.setPrenom(updatedPatient.getPrenom());
            patient.setNom(updatedPatient.getNom());
            patient.setDateNaissance(updatedPatient.getDateNaissance());
            patient.setGenre(updatedPatient.getGenre());
            patient.setAdressePostale(updatedPatient.getAdressePostale());
            patient.setNumeroTelephone(updatedPatient.getNumeroTelephone());
            Patient saved = patientRepository.save(patient);
            logger.info("Patient mis à jour, id : {}", saved.getId());
            return ResponseEntity.ok(saved);
        } else {
            logger.warn("Patient non trouvé pour l'id : {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}


