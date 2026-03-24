package nttdata.bootcamp.debit_cards_service.Rules;

import nttdata.bootcamp.debit_cards_service.Dto.AccountDto;
import nttdata.bootcamp.debit_cards_service.Entity.DebitCardAccountLink;
import nttdata.bootcamp.debit_cards_service.Entity.DebitCardDocument;
import nttdata.bootcamp.debit_cards_service.Exception.BusinessRuleException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Default implementation of {@link DebitCardAccountRulesPolicy} for linked account consistency.
 */
@Component
public class DebitCardAccountRules implements DebitCardAccountRulesPolicy {

    /**
     * Validates account status and ownership before linking.
     *
     * @param card target debit card
     * @param account account to link
     * @throws BusinessRuleException if the account is not active or does not belong to the card's customer
     */
    @Override
    public void validateAccountForLink(DebitCardDocument card, AccountDto account) {
        if (account.getStatus() != null && !"ACTIVE".equalsIgnoreCase(account.getStatus())) {
            throw new BusinessRuleException("Account is not active");
        }
        if (!Objects.equals(account.getCustomerId(), card.getCustomerId())) {
            throw new BusinessRuleException("Account does not belong to the debit card customer");
        }
    }

    /**
     * Adds or updates a link and enforces a single primary account when requested.
     *
     * @param currentLinks existing links
     * @param accountId account id to link
     * @param isPrimaryRequest whether this account should be primary
     * @return updated link list
     */
    @Override
    public List<DebitCardAccountLink> applyLinkRules(List<DebitCardAccountLink> currentLinks,
                                                     String accountId,
                                                     Boolean isPrimaryRequest) {
        List<DebitCardAccountLink> links =
                currentLinks != null ? new ArrayList<>(currentLinks) : new ArrayList<>();

        boolean exists = links.stream().anyMatch(l -> Objects.equals(l.getAccountId(), accountId));
        if (!exists) {
            links.add(new DebitCardAccountLink(accountId, Boolean.TRUE.equals(isPrimaryRequest)));
        }

        if (Boolean.TRUE.equals(isPrimaryRequest)) {
            links.forEach(l -> {
                if (!Objects.equals(l.getAccountId(), accountId)) {
                    l.setIsPrimary(false);
                } else {
                    l.setIsPrimary(true);
                }
            });
        } else if (links.stream().noneMatch(l -> Boolean.TRUE.equals(l.getIsPrimary()))) {
            links.get(0).setIsPrimary(true);
        }

        return links;
    }

    /**
     * Removes a link and promotes another account to primary if none remains primary.
     *
     * @param currentLinks existing links
     * @param accountId account id to unlink
     * @return updated link list
     */
    @Override
    public List<DebitCardAccountLink> applyUnlinkRules(List<DebitCardAccountLink> currentLinks,
                                                       String accountId) {
        List<DebitCardAccountLink> links =
                currentLinks != null ? new ArrayList<>(currentLinks) : new ArrayList<>();

        links.removeIf(l -> Objects.equals(l.getAccountId(), accountId));

        if (!links.isEmpty() && links.stream().noneMatch(l -> Boolean.TRUE.equals(l.getIsPrimary()))) {
            links.get(0).setIsPrimary(true);
        }

        return links;
    }
}
