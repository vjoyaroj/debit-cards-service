package nttdata.bootcamp.debit_cards_service.Mapper;

import com.bank.debitcard.model.DebitCardCreateRequest;
import com.bank.debitcard.model.DebitCardResponse;
import com.bank.debitcard.model.DebitCardUpdateRequest;
import nttdata.bootcamp.debit_cards_service.Entity.DebitCardAccountLink;
import nttdata.bootcamp.debit_cards_service.Entity.DebitCardDocument;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link DebitCardMapper}: document creation, updates, and API response mapping.
 */
class DebitCardMapperTest {

    private final DebitCardMapper mapper = new DebitCardMapper();

    /**
     * New documents should be ACTIVE with empty linked accounts and assigned card number.
     */
    @Test
    void toDocument_createsActiveCard() {
        DebitCardCreateRequest req = new DebitCardCreateRequest("cust-1");
        DebitCardDocument doc = mapper.toDocument(req, "4111");
        assertNotNull(doc.getId());
        assertEquals("4111", doc.getCardNumber());
        assertEquals("ACTIVE", doc.getStatus());
        assertTrue(doc.getLinkedAccounts().isEmpty());
    }

    /**
     * Update should set customer id and status from the request.
     */
    @Test
    void updateDocument_setsCustomerAndStatus() {
        DebitCardDocument doc = new DebitCardDocument();
        DebitCardUpdateRequest u = new DebitCardUpdateRequest("c2", DebitCardUpdateRequest.StatusEnum.BLOCKED);
        mapper.updateDocument(doc, u);
        assertEquals("c2", doc.getCustomerId());
        assertEquals("BLOCKED", doc.getStatus());
    }

    /**
     * Response mapping should work when linked accounts are null.
     */
    @Test
    void toResponse_withoutLinkedAccounts() {
        DebitCardDocument doc = DebitCardDocument.builder()
                .id("dc-2")
                .customerId("c")
                .cardNumber("4999")
                .status("INACTIVE")
                .linkedAccounts(null)
                .createdAt(Instant.parse("2024-02-01T00:00:00Z"))
                .build();

        DebitCardResponse r = mapper.toResponse(doc);
        assertEquals(DebitCardResponse.StatusEnum.INACTIVE, r.getStatus());
        assertTrue(r.getLinkedAccounts() == null || r.getLinkedAccounts().isEmpty());
    }

    /**
     * Linked accounts should map to API link models with primary flag.
     */
    @Test
    void toResponse_mapsLinks() {
        Instant created = Instant.parse("2024-03-01T10:00:00Z");
        DebitCardDocument doc = DebitCardDocument.builder()
                .id("dc-1")
                .customerId("c")
                .cardNumber("4222")
                .status("ACTIVE")
                .linkedAccounts(List.of(new DebitCardAccountLink("acc-1", true)))
                .createdAt(created)
                .build();

        DebitCardResponse r = mapper.toResponse(doc);
        assertEquals("dc-1", r.getId());
        assertEquals(DebitCardResponse.StatusEnum.ACTIVE, r.getStatus());
        assertEquals(OffsetDateTime.ofInstant(created, ZoneOffset.UTC), r.getCreatedAt());
        assertEquals("acc-1", r.getLinkedAccounts().get(0).getAccountId());
        assertTrue(r.getLinkedAccounts().get(0).getIsPrimary());
    }
}
