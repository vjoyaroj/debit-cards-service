package nttdata.bootcamp.debit_cards_service.Client;

import nttdata.bootcamp.debit_cards_service.Dto.AccountDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * HTTP client for the external accounts service with reactive circuit breaker.
 */
@Component
public class AccountClient {

    private final WebClient webClient;
    private final ReactiveCircuitBreaker circuitBreaker;

    /**
     * @param builder shared WebClient builder
     * @param accountServiceUrl base URL for the accounts API
     * @param circuitBreakerFactory factory for the account-service circuit breaker
     */
    public AccountClient(WebClient.Builder builder,
                         @Value("${account.service.url:http://localhost:8082/api/v1}") String accountServiceUrl,
                         ReactiveCircuitBreakerFactory<?, ?> circuitBreakerFactory) {
        this.webClient = builder.baseUrl(accountServiceUrl).build();
        this.circuitBreaker = circuitBreakerFactory.create("accountServiceCb");
    }

    /**
     * Fetches an account by id.
     *
     * @param id account identifier
     * @return account DTO or error on fallback
     */
    public Mono<AccountDto> getAccountById(String id) {
        return circuitBreaker.run(
                webClient.get()
                        .uri("/accounts/{id}", id)
                        .retrieve()
                        .bodyToMono(AccountDto.class),
                throwable -> Mono.error(new RuntimeException(
                        "Fallback: Account Service is currently unavailable. Details: " + throwable.getMessage()))
        );
    }
}
