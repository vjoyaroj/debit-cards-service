package nttdata.bootcamp.debit_cards_service.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * HTTP client beans for reactive outbound calls.
 */
@Configuration
public class WebClientConfig {

    /**
     * @return reusable {@link WebClient.Builder}
     */
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
