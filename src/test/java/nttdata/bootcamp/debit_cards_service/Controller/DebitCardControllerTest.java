package nttdata.bootcamp.debit_cards_service.Controller;

import com.bank.debitcard.model.DebitCardAccountLink;
import com.bank.debitcard.model.DebitCardCreateRequest;
import com.bank.debitcard.model.DebitCardResponse;
import com.bank.debitcard.model.DebitCardUpdateRequest;
import com.bank.debitcard.model.LinkAccountRequest;
import com.bank.debitcard.model.UnlinkAccountRequest;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import nttdata.bootcamp.debit_cards_service.Service.DebitCardService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link DebitCardController}: verifies HTTP status mapping when delegating to {@link DebitCardService}.
 */
class DebitCardControllerTest {

    /**
     * {@code GET} by id should return 404 when the service returns empty.
     */
    @Test
    void getDebitCardByIdReturnsNotFoundWhenEmpty() {
        DebitCardService service = mock(DebitCardService.class);
        when(service.getById("x")).thenReturn(Maybe.empty());
        DebitCardController controller = new DebitCardController(service);

        ResponseEntity<DebitCardResponse> response = controller.getDebitCardById("x").blockingGet();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * {@code GET} by id should return 200 when the card exists.
     */
    @Test
    void getDebitCardByIdReturnsOkWhenPresent() {
        DebitCardService service = mock(DebitCardService.class);
        DebitCardResponse body = new DebitCardResponse();
        body.setId("dc-1");
        when(service.getById("dc-1")).thenReturn(Maybe.just(body));
        DebitCardController controller = new DebitCardController(service);

        ResponseEntity<DebitCardResponse> response = controller.getDebitCardById("dc-1").blockingGet();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("dc-1", response.getBody().getId());
    }

    /**
     * {@code POST} should return 201 Created.
     */
    @Test
    void createDebitCardReturnsCreatedStatus() {
        DebitCardService service = mock(DebitCardService.class);
        DebitCardResponse created = new DebitCardResponse();
        created.setId("id-1");
        when(service.create(any(DebitCardCreateRequest.class))).thenReturn(Single.just(created));
        DebitCardController controller = new DebitCardController(service);

        ResponseEntity<DebitCardResponse> response = controller.createDebitCard(new DebitCardCreateRequest()).blockingGet();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("id-1", response.getBody().getId());
    }

    /**
     * {@code GET} all should return the list from the service with 200 OK.
     */
    @Test
    void getDebitCardsReturnsListFromService() {
        DebitCardService service = mock(DebitCardService.class);
        when(service.findAll()).thenReturn(Single.just(List.of(new DebitCardResponse())));
        DebitCardController controller = new DebitCardController(service);

        ResponseEntity<List<DebitCardResponse>> response = controller.getDebitCards().blockingGet();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    /**
     * Unlink account endpoint should return 200 OK.
     */
    @Test
    void unlinkAccountReturnsOk() {
        DebitCardService service = mock(DebitCardService.class);
        DebitCardResponse body = new DebitCardResponse();
        UnlinkAccountRequest req = new UnlinkAccountRequest("acc-9");
        when(service.unlinkAccount(eq("dc-3"), any(UnlinkAccountRequest.class))).thenReturn(Single.just(body));
        DebitCardController controller = new DebitCardController(service);

        ResponseEntity<DebitCardResponse> response = controller.unlinkAccountFromDebitCard("dc-3", req).blockingGet();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Link account endpoint should return 200 OK with body id.
     */
    @Test
    void linkAccountReturnsOk() {
        DebitCardService service = mock(DebitCardService.class);
        DebitCardResponse body = new DebitCardResponse();
        body.setId("dc-2");
        LinkAccountRequest req = new LinkAccountRequest("acc-1");
        when(service.linkAccount(eq("dc-2"), any(LinkAccountRequest.class))).thenReturn(Single.just(body));
        DebitCardController controller = new DebitCardController(service);

        ResponseEntity<DebitCardResponse> response = controller.linkAccountToDebitCard("dc-2", req).blockingGet();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("dc-2", response.getBody().getId());
    }

    /**
     * List linked accounts should return 200 OK with the links from the service.
     */
    @Test
    void getDebitCardAccountsReturnsOk() {
        DebitCardService service = mock(DebitCardService.class);
        when(service.getAccounts("dc")).thenReturn(Single.just(List.of(new DebitCardAccountLink())));
        DebitCardController controller = new DebitCardController(service);

        ResponseEntity<List<DebitCardAccountLink>> response = controller.getDebitCardAccounts("dc").blockingGet();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    /**
     * Update should return 200 OK with updated body.
     */
    @Test
    void updateDebitCardReturnsOk() {
        DebitCardService service = mock(DebitCardService.class);
        DebitCardResponse body = new DebitCardResponse();
        body.setId("u1");
        DebitCardUpdateRequest req = new DebitCardUpdateRequest("c", DebitCardUpdateRequest.StatusEnum.INACTIVE);
        when(service.update(eq("u1"), any(DebitCardUpdateRequest.class))).thenReturn(Single.just(body));
        DebitCardController controller = new DebitCardController(service);

        ResponseEntity<DebitCardResponse> response = controller.updateDebitCard("u1", req).blockingGet();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("u1", response.getBody().getId());
    }

    /**
     * Delete should return 204 No Content.
     */
    @Test
    void deleteDebitCardReturnsNoContent() {
        DebitCardService service = mock(DebitCardService.class);
        when(service.delete(eq("abc"))).thenReturn(Completable.complete());
        DebitCardController controller = new DebitCardController(service);

        ResponseEntity<Void> response = controller.deleteDebitCard("abc").blockingGet();
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
