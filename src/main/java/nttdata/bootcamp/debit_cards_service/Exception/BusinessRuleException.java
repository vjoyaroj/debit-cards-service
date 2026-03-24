package nttdata.bootcamp.debit_cards_service.Exception;

/**
 * Raised when a domain business rule is violated (e.g. invalid account link).
 */
public class BusinessRuleException extends RuntimeException {
    /**
     * @param message detail for API consumers
     */
    public BusinessRuleException(String message) {
        super(message);
    }
}
