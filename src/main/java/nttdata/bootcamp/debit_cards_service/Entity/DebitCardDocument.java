package nttdata.bootcamp.debit_cards_service.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

/**
 * MongoDB persistence model for a debit card and its linked accounts.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "debit_cards")
public class DebitCardDocument {

    @Id
    private String id;

    private String customerId;

    private String cardNumber;

    private String status;

    private List<DebitCardAccountLink> linkedAccounts;

    private Instant createdAt;
}
