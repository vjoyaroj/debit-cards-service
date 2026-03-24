package nttdata.bootcamp.debit_cards_service.Service;

import com.bank.debitcard.model.DebitCardAccountLink;
import com.bank.debitcard.model.DebitCardCreateRequest;
import com.bank.debitcard.model.DebitCardResponse;
import com.bank.debitcard.model.DebitCardUpdateRequest;
import com.bank.debitcard.model.LinkAccountRequest;
import com.bank.debitcard.model.UnlinkAccountRequest;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

import java.util.List;

/**
 * Business operations for debit cards and their linked accounts.
 */
public interface DebitCardService {

    /**
     * Lists debit cards with ACTIVE status (or as defined by the repository).
     *
     * @return list of debit card responses
     */
    Single<List<DebitCardResponse>> findAll();

    /**
     * Creates a debit card; card number is assigned by the system.
     *
     * @param request creation payload
     * @return created card
     */
    Single<DebitCardResponse> create(DebitCardCreateRequest request);

    /**
     * Finds a debit card by id (may use cache).
     *
     * @param id debit card id
     * @return card if present
     */
    Maybe<DebitCardResponse> getById(String id);

    /**
     * Updates customer id and status.
     *
     * @param id debit card id
     * @param request update payload
     * @return updated card
     */
    Single<DebitCardResponse> update(String id, DebitCardUpdateRequest request);

    /**
     * Logical delete (marks INACTIVE).
     *
     * @param id debit card id
     * @return completion signal
     */
    Completable delete(String id);

    /**
     * Links a bank account to the card after validation.
     *
     * @param id debit card id
     * @param request link payload
     * @return updated card
     */
    Single<DebitCardResponse> linkAccount(String id, LinkAccountRequest request);

    /**
     * Unlinks a bank account from the card.
     *
     * @param id debit card id
     * @param request unlink payload
     * @return updated card
     */
    Single<DebitCardResponse> unlinkAccount(String id, UnlinkAccountRequest request);

    /**
     * Returns linked accounts for a debit card as API link models.
     *
     * @param id debit card id
     * @return list of account links
     */
    Single<List<DebitCardAccountLink>> getAccounts(String id);
}
