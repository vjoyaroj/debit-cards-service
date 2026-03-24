package nttdata.bootcamp.debit_cards_service.Service;

import nttdata.bootcamp.debit_cards_service.Entity.SequenceDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Generates unique sequential debit card numbers using the {@code sequences} collection in MongoDB.
 */
@Service
public class DebitCardNumberSequence {

    private static final String SEQUENCE_KEY = "debitCardNumber";

    private final ReactiveMongoTemplate mongoTemplate;

    @Value("${debit-cards.number.prefix:DC-}")
    private String prefix;

    @Value("${debit-cards.number.width:8}")
    private int width;

    /**
     * @param mongoTemplate reactive Mongo template for find-and-modify
     */
    public DebitCardNumberSequence(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Returns the next formatted card number (prefix + zero-padded sequence).
     *
     * @return new card number string
     */
    public Mono<String> nextCardNumber() {
        Query query = Query.query(Criteria.where("_id").is(SEQUENCE_KEY));
        Update update = new Update().inc("seq", 1);
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(true);
        return mongoTemplate.findAndModify(query, update, options, SequenceDocument.class)
                .map(doc -> prefix + String.format("%0" + width + "d", doc.getSeq()));
    }
}
