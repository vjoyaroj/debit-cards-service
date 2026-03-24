package nttdata.bootcamp.debit_cards_service.Repository;

import nttdata.bootcamp.debit_cards_service.Entity.DebitCardDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

/**
 * Reactive MongoDB repository for {@link DebitCardDocument}.
 */
public interface DebitCardRepository extends ReactiveMongoRepository<DebitCardDocument, String> {

    /**
     * Finds debit cards by status (e.g. ACTIVE).
     *
     * @param status status filter
     * @return matching documents
     */
    Flux<DebitCardDocument> findByStatus(String status);
}
