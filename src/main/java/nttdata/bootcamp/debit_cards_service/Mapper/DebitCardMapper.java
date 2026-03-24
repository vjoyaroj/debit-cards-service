package nttdata.bootcamp.debit_cards_service.Mapper;

import com.bank.debitcard.model.DebitCardAccountLink;
import com.bank.debitcard.model.DebitCardCreateRequest;
import com.bank.debitcard.model.DebitCardResponse;
import com.bank.debitcard.model.DebitCardUpdateRequest;
import nttdata.bootcamp.debit_cards_service.Entity.DebitCardDocument;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Maps between OpenAPI models and {@link DebitCardDocument} persistence entities.
 */
@Component
public class DebitCardMapper {

    /**
     * Creates a new document; {@code cardNumber} is assigned by the system.
     *
     * @param request create request
     * @param cardNumber generated card number
     * @return document ready to save
     */
    public DebitCardDocument toDocument(DebitCardCreateRequest request, String cardNumber) {
        DebitCardDocument doc = new DebitCardDocument();
        doc.setId(UUID.randomUUID().toString());
        doc.setCustomerId(request.getCustomerId());
        doc.setCardNumber(cardNumber);
        doc.setStatus("ACTIVE");
        doc.setLinkedAccounts(new ArrayList<>());
        doc.setCreatedAt(java.time.Instant.now());
        return doc;
    }

    /**
     * Applies PUT changes (customer id and status).
     *
     * @param doc document to mutate
     * @param request update payload
     */
    public void updateDocument(DebitCardDocument doc, DebitCardUpdateRequest request) {
        doc.setCustomerId(request.getCustomerId());
        doc.setStatus(request.getStatus().getValue());
    }

    /**
     * Converts an internal document to the API response, including linked accounts.
     *
     * @param doc persisted debit card
     * @return API response
     */
    public DebitCardResponse toResponse(DebitCardDocument doc) {
        DebitCardResponse response = new DebitCardResponse();
        response.setId(doc.getId());
        response.setCustomerId(doc.getCustomerId());
        response.setCardNumber(doc.getCardNumber());
        if (doc.getStatus() != null) {
            response.setStatus(DebitCardResponse.StatusEnum.fromValue(doc.getStatus()));
        }
        if (doc.getCreatedAt() != null) {
            response.setCreatedAt(OffsetDateTime.ofInstant(doc.getCreatedAt(), ZoneOffset.UTC));
        }
        if (doc.getLinkedAccounts() != null) {
            response.setLinkedAccounts(doc.getLinkedAccounts().stream().map(link -> {
                DebitCardAccountLink apiLink = new DebitCardAccountLink();
                apiLink.setAccountId(link.getAccountId());
                apiLink.setIsPrimary(Boolean.TRUE.equals(link.getIsPrimary()));
                return apiLink;
            }).toList());
        }
        return response;
    }
}
