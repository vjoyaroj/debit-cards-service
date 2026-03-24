package nttdata.bootcamp.debit_cards_service.Rules;

import nttdata.bootcamp.debit_cards_service.Dto.AccountDto;
import nttdata.bootcamp.debit_cards_service.Entity.DebitCardAccountLink;
import nttdata.bootcamp.debit_cards_service.Entity.DebitCardDocument;
import nttdata.bootcamp.debit_cards_service.Exception.BusinessRuleException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link DebitCardAccountRules}: validation and link list mutations.
 */
class DebitCardAccountRulesTest {

    private final DebitCardAccountRules rules = new DebitCardAccountRules();

    /**
     * Inactive accounts must not be linked.
     */
    @Test
    void validateAccountForLink_rejectsInactiveAccount() {
        DebitCardDocument card = new DebitCardDocument();
        card.setCustomerId("c1");
        AccountDto account = new AccountDto();
        account.setCustomerId("c1");
        account.setStatus("CLOSED");

        assertThrows(BusinessRuleException.class, () -> rules.validateAccountForLink(card, account));
    }

    /**
     * Link rules should add a new account entry when none exist.
     */
    @Test
    void applyLinkRules_addsNewAccount() {
        List<DebitCardAccountLink> out = rules.applyLinkRules(null, "a1", true);
        assertEquals(1, out.size());
        assertEquals("a1", out.get(0).getAccountId());
    }
}
