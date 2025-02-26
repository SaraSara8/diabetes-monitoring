package com.diabetes.patient.controller;

import com.diabetes.patient.model.Patient;
import com.diabetes.patient.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;

    /**
     * Récupère une page de patients.
     * Si le paramètre "query" est fourni, effectue une recherche par nom ou prénom.
     */
    @GetMapping
    public ResponseEntity<Page<Patient>> getPatients(
            @RequestParam(value = "query", required = false) String query,
            Pageable pageable) {
        Page<Patient> patients;
        if (query != null && !query.trim().isEmpty()) {
            patients = patientRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(query, query, pageable);
        } else {
            patients = patientRepository.findAll(pageable);
        }
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable String id) {
        Optional<Patient> patient = patientRepository.findById(id);
        return patient.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        Patient savedPatient = patientRepository.save(patient);
        return ResponseEntity.ok(savedPatient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable String id, @RequestBody Patient updatedPatient) {
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
            return ResponseEntity.ok(saved);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
