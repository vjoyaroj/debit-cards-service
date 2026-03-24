package nttdata.bootcamp.debit_cards_service.Dto;

import lombok.Data;

/**
 * Lightweight account projection returned by accounts-service for link validation.
 */
@Data
public class AccountDto {
    private String id;
    private String customerId;
    private String type;
    private Double balance;
    private String status;
}
