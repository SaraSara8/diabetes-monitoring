package com.diabetes.notes;

import com.diabetes.notes.model.Note;
import com.diabetes.notes.repository.NoteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


/**
 * Composant Spring qui précharge des données de test dans la collection de notes au démarrage de l'application.
 * Si la collection est vide, plusieurs notes de test sont créées et sauvegardées pour différents patients.
 */

@Component
public class DataLoader implements CommandLineRunner {

    private final NoteRepository noteRepository;

    public DataLoader(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // On insère les notes uniquement si la collection est vide
        if (noteRepository.count() == 0) {

            // 1) patientId=1 (TestNone)
            Note note1 = new Note();
            note1.setPatientId("1");
            note1.setContent("Le patient déclare qu'il se sent très bien! Poids égal ou inférieur au poids recommandé");
            note1.setCreatedAt(LocalDateTime.now());
            noteRepository.save(note1);

            // 2) patientId=2 (TestBorderline)
            Note note2 = new Note();
            note2.setPatientId("2");
            note2.setContent("Le patient déclare qu'il ressent beaucoup de stress au travail Il se plaint " +
                    "également que son audition est anormale dernièremene");
            note2.setCreatedAt(LocalDateTime.now());
            noteRepository.save(note2);


            // 3) patientId=2 (TestBorderline)
            Note note3 = new Note();
            note3.setPatientId("2");
            note3.setContent("Le patient déclare avoir fait une réaction aux médicaments au cours " +
                    "des 3 derniers mois Il remarque également que son audition continue d'être anormale");
            note3.setCreatedAt(LocalDateTime.now());
            noteRepository.save(note3);


            // 4) patientId=3 (TestInDanger)
            Note note4 = new Note();
            note4.setPatientId("3");
            note4.setContent("Le Le patient déclare qu'il fume depuis peu");
            note4.setCreatedAt(LocalDateTime.now());
            noteRepository.save(note4);


            // 5) patientId=3 (TestInDanger)
            Note note5 = new Note();
            note5.setPatientId("3");
            note5.setContent(" Le patient déclare qu'il est fumeur et qu'il a cessé de fumer " +
                    "l'année dernière Il se plaint également de crises d’apnée respiratoire anormales " +
                    "Tests de laboratoire indiquant un taux de cholestérol LDL élevé");
            note5.setCreatedAt(LocalDateTime.now());
            noteRepository.save(note5);

            // 6) patientId=4 (TestEarlyOnset)
            Note note6 = new Note();
            note6.setPatientId("4");
            note6.setContent("Le patient déclare qu'il lui est devenu difficile de monter les escaliers Il se "+
                    "plaint également d’être essoufflé Tests de laboratoire indiquant que les anticorps sont élevés "+
                    "Réaction aux médicaments");
            note6.setCreatedAt(LocalDateTime.now());
            noteRepository.save(note6);



            // 7) patientId=4 (TestEarlyOnset)
            Note note7 = new Note();
            note7.setPatientId("4");
            note7.setContent("Le patient déclare qu'il a mal au dos lorsqu'il reste assis pendant longtemps");
            note7.setCreatedAt(LocalDateTime.now());
            noteRepository.save(note7);

            // 8) patientId=4 (TestEarlyOnset)
            Note note8 = new Note();
            note8.setPatientId("4");
            note8.setContent("Le patient déclare avoir commencé à fumer depuis peu Hémoglobine A1C supérieure " +
                    "au niveau recommandé");
            note8.setCreatedAt(LocalDateTime.now());
            noteRepository.save(note8);

            // 9) patientId=4 (TestEarlyOnset)
            Note note9 = new Note();
            note9.setPatientId("4");
            note9.setContent("Taille, Poids, Cholestérol, Vertige de Réaction");
            note9.setCreatedAt(LocalDateTime.now());
            noteRepository.save(note9);
        }
    }
}
