package com.diabetes.patient;

import com.diabetes.patient.model.Patient;
import com.diabetes.patient.service.PatientService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;


/**
 * Composant Spring qui précharge des données de test pour l'entité Patient au démarrage de l'application.
 * Si la collection est vide, il ajoute quelques patients de test via le PatientService, qui gère également la génération d'identifiants auto-incrémentés.
 */


@Component
public class DataLoader implements CommandLineRunner {

    private final PatientService patientService;

    // Injecter PatientService pour utiliser createPatient() qui gère l'id
    public DataLoader(PatientService patientService) {
        this.patientService = patientService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Si la collection est vide, ajouter quelques patients de test via le service
        if (patientService.getAllPatients().isEmpty()) {
            patientService.createPatient(new Patient("TestNone", "Test", LocalDate.of(1966, 12, 31), "F", "1 Brookside St", "100-222-3333"));
            patientService.createPatient(new Patient("TestBorderline", "Test", LocalDate.of(1945, 6, 24), "M", "2 High St", "200-333-4444"));
            patientService.createPatient(new Patient("TestInDanger", "Test", LocalDate.of(2004, 6, 18), "M", "3 Club Road", "300-444-5555"));
            patientService.createPatient(new Patient("TestEarlyOnset", "Test", LocalDate.of(2002, 6, 28), "F", "4 Valley Dr", "400-555-6666"));
        }
    }
}
