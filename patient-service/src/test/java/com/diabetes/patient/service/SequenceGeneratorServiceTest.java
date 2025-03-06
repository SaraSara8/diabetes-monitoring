package com.diabetes.patient.service;


import com.diabetes.patient.sequence.DatabaseSequence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SequenceGeneratorServiceTest {

    @Mock
    private MongoOperations mongoOperations;

    @InjectMocks
    private SequenceGeneratorService sequenceGeneratorService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        // Simuler que la collection existe pour éviter la création durant le test
        when(mongoOperations.collectionExists(DatabaseSequence.class)).thenReturn(true);
    }

    @Test
    public void testGenerateSequence_FirstTime() {
        // Simuler le comportement de findAndModify pour retourner une séquence initiale avec valeur 1
        DatabaseSequence sequence = new DatabaseSequence();
        sequence.setId("patients_sequence");
        sequence.setSeq(1L);

        when(mongoOperations.findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                any(Class.class)
        )).thenReturn(sequence);

        long seqValue = sequenceGeneratorService.generateSequence("patients_sequence");
        assertThat(seqValue).isEqualTo(1L);
    }

    @Test
    public void testGenerateSequence_NonZero() {
        // Simuler le cas où la séquence retourne une valeur différente, par exemple 5
        DatabaseSequence sequence = new DatabaseSequence();
        sequence.setId("patients_sequence");
        sequence.setSeq(5L);

        when(mongoOperations.findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                any(Class.class)
        )).thenReturn(sequence);

        long seqValue = sequenceGeneratorService.generateSequence("patients_sequence");
        assertThat(seqValue).isEqualTo(5L);
    }
}
