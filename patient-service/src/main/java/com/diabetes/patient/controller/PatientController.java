package com.diabetes.patient.controller;


import com.diabetes.patient.model.Patient;
import com.diabetes.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * Contrôleur REST pour la gestion des patients.
 */
@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    /**
     * Récupère la liste de tous les patients.
     * @return liste des patients
     */
    @GetMapping
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    /**
     * Récupère un patient par son identifiant.
     * @param id identifiant du patient
     * @return ResponseEntity contenant le patient ou un statut 404 si non trouvé
     */
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable String id) {
        Optional<Patient> patientOpt = patientService.getPatientById(id);
        if (patientOpt.isPresent()) {
            return ResponseEntity.ok(patientOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Crée un nouveau patient.
     * @param patient objet Patient à créer
     * @return le patient créé
     */
    @PostMapping
    public Patient createPatient(@RequestBody Patient patient) {
        return patientService.createPatient(patient);
    }

    /**
     * Met à jour un patient existant.
     * @param id identifiant du patient à mettre à jour
     * @param patient objet contenant les nouvelles informations
     * @return ResponseEntity contenant le patient mis à jour ou un statut 404 si non trouvé
     */
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable String id, @RequestBody Patient patient) {
        Patient updatedPatient = patientService.updatePatient(id, patient);
        if (updatedPatient != null) {
            return ResponseEntity.ok(updatedPatient);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
