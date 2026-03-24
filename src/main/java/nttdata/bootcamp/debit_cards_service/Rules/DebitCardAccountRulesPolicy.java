package nttdata.bootcamp.debit_cards_service.Rules;

import nttdata.bootcamp.debit_cards_service.Dto.AccountDto;
import nttdata.bootcamp.debit_cards_service.Entity.DebitCardAccountLink;
import nttdata.bootcamp.debit_cards_service.Entity.DebitCardDocument;

import java.util.List;

/**
 * Business rules for linking and unlinking bank accounts on debit cards.
 */
public interface DebitCardAccountRulesPolicy {
    /**
     * Validates whether an account may be linked to a debit card.
     *
     * @param card target debit card
     * @param account account to validate
     * @throws nttdata.bootcamp.debit_cards_service.Exception.BusinessRuleException when rules are violated
     */
    void validateAccountForLink(DebitCardDocument card, AccountDto account);

    /**
     * Applies rules when adding or adjusting a linked account (including primary flag).
     *
     * @param currentLinks existing links (may be null)
     * @param accountId account to link
     * @param isPrimaryRequest whether this link should become primary
     * @return updated link list to persist
     */
    List<DebitCardAccountLink> applyLinkRules(List<DebitCardAccountLink> currentLinks,
                                              String accountId,
                                              Boolean isPrimaryRequest);

    /**
     * Applies rules when removing a linked account (reassigns primary if needed).
     *
     * @param currentLinks existing links (may be null)
     * @param accountId account to remove
     * @return updated link list to persist
     */
    List<DebitCardAccountLink> applyUnlinkRules(List<DebitCardAccountLink> currentLinks,
                                                String accountId);
}
