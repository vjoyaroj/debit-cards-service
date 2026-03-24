package nttdata.bootcamp.debit_cards_service.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Embedded link between a debit card and a bank account id, with optional primary flag.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebitCardAccountLink {
    private String accountId;
    private Boolean isPrimary;
}
