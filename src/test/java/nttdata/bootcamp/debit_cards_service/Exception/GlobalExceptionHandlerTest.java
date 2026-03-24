package nttdata.bootcamp.debit_cards_service.Exception;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link GlobalExceptionHandler} HTTP status mapping.
 */
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    /**
     * {@link IllegalArgumentException} maps to 400 Bad Request.
     */
    @Test
    void handleIllegalArgument_returnsBadRequest() {
        ResponseEntity<Map<String, String>> res =
                handler.handleIllegalArgumentException(new IllegalArgumentException("bad"));
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    /**
     * Constraint violations map to 400 Bad Request.
     */
    @Test
    void handleConstraintViolation_returnsBadRequest() {
        ResponseEntity<Map<String, String>> res =
                handler.handleConstraintViolationException(
                        new ConstraintViolationException("v", null));
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    /**
     * {@link BusinessRuleException} maps to HTTP 422.
     */
    @Test
    void handleBusinessRule_returns422() {
        ResponseEntity<Map<String, String>> res =
                handler.handleBusinessRuleException(new BusinessRuleException("rule"));
        assertEquals(422, res.getStatusCode().value());
    }

    /**
     * Generic runtime errors map to 404 in this handler.
     */
    @Test
    void handleRuntime_returnsNotFound() {
        ResponseEntity<Map<String, String>> res =
                handler.handleRuntimeException(new RuntimeException("x"));
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }
}
