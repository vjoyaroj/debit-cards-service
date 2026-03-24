package nttdata.bootcamp.debit_cards_service.Controller;

import com.bank.debitcard.api.DebitCardsApi;
import com.bank.debitcard.model.DebitCardAccountLink;
import com.bank.debitcard.model.DebitCardCreateRequest;
import com.bank.debitcard.model.DebitCardResponse;
import com.bank.debitcard.model.DebitCardUpdateRequest;
import com.bank.debitcard.model.LinkAccountRequest;
import com.bank.debitcard.model.UnlinkAccountRequest;
import io.reactivex.rxjava3.core.Single;
import lombok.RequiredArgsConstructor;
import nttdata.bootcamp.debit_cards_service.Service.DebitCardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Reactive REST controller for debit cards and linked bank accounts (OpenAPI implementation).
 */
@RestController
@RequiredArgsConstructor
public class DebitCardController implements DebitCardsApi {

    private final DebitCardService debitCardService;

    /**
     * {@inheritDoc}
     */
    @Override
    public Single<ResponseEntity<List<DebitCardResponse>>> getDebitCards() {
        return debitCardService.findAll().map(ResponseEntity::ok);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Single<ResponseEntity<DebitCardResponse>> createDebitCard(DebitCardCreateRequest debitCardCreateRequest) {
        return debitCardService.create(debitCardCreateRequest)
                .map(card -> ResponseEntity.status(HttpStatus.CREATED).body(card));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Single<ResponseEntity<DebitCardResponse>> getDebitCardById(String id) {
        return debitCardService.getById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Single<ResponseEntity<DebitCardResponse>> updateDebitCard(String id, DebitCardUpdateRequest debitCardUpdateRequest) {
        return debitCardService.update(id, debitCardUpdateRequest)
                .map(ResponseEntity::ok);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Single<ResponseEntity<Void>> deleteDebitCard(String id) {
        return debitCardService.delete(id)
                .toSingleDefault(ResponseEntity.noContent().<Void>build());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Single<ResponseEntity<DebitCardResponse>> linkAccountToDebitCard(String id, LinkAccountRequest linkAccountRequest) {
        return debitCardService.linkAccount(id, linkAccountRequest)
                .map(ResponseEntity::ok);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Single<ResponseEntity<DebitCardResponse>> unlinkAccountFromDebitCard(String id, UnlinkAccountRequest unlinkAccountRequest) {
        return debitCardService.unlinkAccount(id, unlinkAccountRequest)
                .map(ResponseEntity::ok);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Single<ResponseEntity<List<DebitCardAccountLink>>> getDebitCardAccounts(String id) {
        return debitCardService.getAccounts(id)
                .map(ResponseEntity::ok);
    }
}
