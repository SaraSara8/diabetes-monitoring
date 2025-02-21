package com.diabetes.patient;


import com.diabetes.patient.model.Patient;
import com.diabetes.patient.repository.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

/**
 * Classe pour précharger des données de test dans la base de données.
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final PatientRepository patientRepository;

    public DataLoader(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }


    /**
     * Méthode de type CommandLineRunner permettant d'insérer des données
     * de test à chaque démarrage de l'application.
     *
     * @return un objet CommandLineRunner
     */

    @Override
    public void run(String... args) throws Exception {
        // Si la collection est vide, ajouter quelques patients de test
        if (patientRepository.count() == 0) {
            patientRepository.save(new Patient("Jean", "Dupont", LocalDate.of(1970, 5, 20), "M", "123 Rue Principale", "0102030405"));
            patientRepository.save(new Patient("Marie", "Curie", LocalDate.of(1980, 3, 15), "F", "456 Avenue de la République", "0607080910"));
            patientRepository.save(new Patient("Luc", "Martin", LocalDate.of(1990, 12, 10), "M", null, null));
            patientRepository.save(new Patient("Sophie", "Lefevre", LocalDate.of(2000, 7, 25), "F", "789 Boulevard Victor Hugo", "1122334455"));
        }
    }
}
