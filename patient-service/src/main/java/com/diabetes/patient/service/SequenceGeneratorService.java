


package com.diabetes.patient.service;

import com.diabetes.patient.sequence.DatabaseSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class SequenceGeneratorService {

    private static final Logger logger = LoggerFactory.getLogger(SequenceGeneratorService.class);

    @Autowired
    @Lazy
    private MongoOperations mongoOperations;

    /**
     * Génère la prochaine valeur de séquence pour le nom de séquence spécifié.
     * Par exemple, pour "patients_sequence", la première valeur retournée sera 1, puis 2, etc.
     *
     * @param seqName le nom de la séquence (ex. "patients_sequence")
     * @return la valeur incrémentée de la séquence
     */
    public long generateSequence(String seqName) {
        logger.debug("Génération de la séquence pour : {}", seqName);

        // Vérifier si la collection pour les séquences existe ; sinon la créer.
        if (!mongoOperations.collectionExists(DatabaseSequence.class)) {
            mongoOperations.createCollection(DatabaseSequence.class);
            logger.debug("Collection 'database_sequences' créée.");
        }

        DatabaseSequence counter = mongoOperations.findAndModify(
                query(where("_id").is(seqName)),
                new Update().inc("seq", 1),
                FindAndModifyOptions.options().returnNew(true).upsert(true),
                DatabaseSequence.class);

        long seqValue = (counter != null) ? counter.getSeq() : 1;
        logger.debug("Nouvelle valeur de séquence pour {} : {}", seqName, seqValue);
        return seqValue;
    }
}


