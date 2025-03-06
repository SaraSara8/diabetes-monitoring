package com.diabetes.patient.service;

import com.diabetes.patient.model.Patient;
import com.diabetes.patient.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private SequenceGeneratorService sequenceGeneratorService;

    @InjectMocks
    private PatientService patientService;

    @BeforeEach
    public void setUp() {
        // Initialise les mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllPatients() {
        // Préparation
        Patient p1 = new Patient("Doe", "John", LocalDate.of(1990, 1, 1), "M", "Address1", "111-111-1111");
        Patient p2 = new Patient("Smith", "Jane", LocalDate.of(1992, 2, 2), "F", "Address2", "222-222-2222");
        when(patientRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        // Exécution
        List<Patient> patients = patientService.getAllPatients();

        // Vérification
        assertThat(patients).hasSize(2);
    }

    @Test
    public void testGetPatientById_Found() {
        // Préparation
        Patient p = new Patient("Doe", "John", LocalDate.of(1990, 1, 1), "M", "Address1", "111-111-1111");
        p.setId("1");
        when(patientRepository.findById("1")).thenReturn(Optional.of(p));

        // Exécution
        Optional<Patient> result = patientService.getPatientById("1");

        // Vérification
        assertThat(result).isPresent();
        assertThat(result.get().getNom()).isEqualTo("Doe");
    }

    @Test
    public void testGetPatientById_NotFound() {
        // Préparation
        when(patientRepository.findById("nonexistent")).thenReturn(Optional.empty());

        // Exécution
        Optional<Patient> result = patientService.getPatientById("nonexistent");

        // Vérification
        assertThat(result).isNotPresent();
    }

    @Test
    public void testCreatePatient() {
        // Préparation d'un patient sans ID
        Patient p = new Patient("Doe", "John", LocalDate.of(1990, 1, 1), "M", "Address1", "111-111-1111");
        // Simulation de la génération de séquence
        when(sequenceGeneratorService.generateSequence("patients_sequence")).thenReturn(1L);
        // Après création, le patient aura l'ID "1"
        Patient pWithId = new Patient("Doe", "John", LocalDate.of(1990, 1, 1), "M", "Address1", "111-111-1111");
        pWithId.setId("1");
        when(patientRepository.save(any(Patient.class))).thenReturn(pWithId);

        // Exécution
        Patient created = patientService.createPatient(p);

        // Vérification
        assertThat(created.getId()).isEqualTo("1");
        assertThat(created.getNom()).isEqualTo("Doe");
    }

    @Test
    public void testUpdatePatient_Found() {
        // Préparation : patient existant
        Patient existing = new Patient("Doe", "John", LocalDate.of(1990, 1, 1), "M", "Address1", "111-111-1111");
        existing.setId("1");
        when(patientRepository.findById("1")).thenReturn(Optional.of(existing));

        // Patient mis à jour
        Patient updated = new Patient("Smith", "Jane", LocalDate.of(1992, 2, 2), "F", "NewAddress", "222-222-2222");
        updated.setId("1");
        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Exécution
        Patient result = patientService.updatePatient("1", updated);

        // Vérification
        assertThat(result).isNotNull();
        assertThat(result.getNom()).isEqualTo("Smith");
        assertThat(result.getAdressePostale()).isEqualTo("NewAddress");
    }

    @Test
    public void testUpdatePatient_NotFound() {
        // Préparation : aucun patient trouvé
        when(patientRepository.findById("nonexistent")).thenReturn(Optional.empty());
        Patient updated = new Patient("Smith", "Jane", LocalDate.of(1992, 2, 2), "F", "NewAddress", "222-222-2222");

        // Exécution
        Patient result = patientService.updatePatient("nonexistent", updated);

        // Vérification
        assertThat(result).isNull();
    }
}
