package nttdata.bootcamp.debit_cards_service.Validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Runs Jakarta Bean Validation and throws a consolidated {@link ConstraintViolationException} on failure.
 */
@Component
@RequiredArgsConstructor
public class ValidationSupport {
    private final Validator validator;

    /**
     * Validates {@code value} and returns it when valid.
     *
     * @param value object to validate
     * @param <T> value type
     * @return the same instance when valid
     * @throws ConstraintViolationException when validation fails
     */
    public <T> T validateOrThrow(T value) {
        Set<ConstraintViolation<T>> violations = validator.validate(value);
        if (!violations.isEmpty()) {
            String message = violations.stream()
                    .map(v -> v.getPropertyPath() + " " + v.getMessage())
                    .collect(Collectors.joining(", "));
            throw new ConstraintViolationException(message, (Set) violations);
        }
        return value;
    }
}
