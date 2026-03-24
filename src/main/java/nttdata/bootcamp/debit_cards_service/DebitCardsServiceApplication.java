package nttdata.bootcamp.debit_cards_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Bootstrap for the Debit Cards microservice.
 */
@SpringBootApplication
public class DebitCardsServiceApplication {
    /**
     * @param args standard Spring Boot arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(DebitCardsServiceApplication.class, args);
    }
}
