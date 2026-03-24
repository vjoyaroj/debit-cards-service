package com.bank.debitcard.model;

import java.net.URI;
import java.util.Objects;
import com.bank.debitcard.model.DebitCardAccountLink;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * DebitCardResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.5.0")
public class DebitCardResponse {

  private String id;

  private String customerId;

  private String cardNumber;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ACTIVE("ACTIVE"),
    
    INACTIVE("INACTIVE"),
    
    BLOCKED("BLOCKED"),
    
    CLOSED("CLOSED");

    private String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private StatusEnum status;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime createdAt;

  @Valid
  private List<@Valid DebitCardAccountLink> linkedAccounts = new ArrayList<>();

  public DebitCardResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DebitCardResponse(String customerId, String cardNumber, StatusEnum status) {
    this.customerId = customerId;
    this.cardNumber = cardNumber;
    this.status = status;
  }

  public DebitCardResponse id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
  */
  
  @Schema(name = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public DebitCardResponse customerId(String customerId) {
    this.customerId = customerId;
    return this;
  }

  /**
   * Get customerId
   * @return customerId
  */
  @NotNull 
  @Schema(name = "customerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("customerId")
  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public DebitCardResponse cardNumber(String cardNumber) {
    this.cardNumber = cardNumber;
    return this;
  }

  /**
   * Generado por el sistema; único y correlativo
   * @return cardNumber
  */
  @NotNull 
  @Schema(name = "cardNumber", description = "Generado por el sistema; único y correlativo", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("cardNumber")
  public String getCardNumber() {
    return cardNumber;
  }

  public void setCardNumber(String cardNumber) {
    this.cardNumber = cardNumber;
  }

  public DebitCardResponse status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
  */
  @NotNull 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public DebitCardResponse createdAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
  */
  @Valid 
  @Schema(name = "createdAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("createdAt")
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public DebitCardResponse linkedAccounts(List<@Valid DebitCardAccountLink> linkedAccounts) {
    this.linkedAccounts = linkedAccounts;
    return this;
  }

  public DebitCardResponse addLinkedAccountsItem(DebitCardAccountLink linkedAccountsItem) {
    if (this.linkedAccounts == null) {
      this.linkedAccounts = new ArrayList<>();
    }
    this.linkedAccounts.add(linkedAccountsItem);
    return this;
  }

  /**
   * Get linkedAccounts
   * @return linkedAccounts
  */
  @Valid 
  @Schema(name = "linkedAccounts", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("linkedAccounts")
  public List<@Valid DebitCardAccountLink> getLinkedAccounts() {
    return linkedAccounts;
  }

  public void setLinkedAccounts(List<@Valid DebitCardAccountLink> linkedAccounts) {
    this.linkedAccounts = linkedAccounts;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DebitCardResponse debitCardResponse = (DebitCardResponse) o;
    return Objects.equals(this.id, debitCardResponse.id) &&
        Objects.equals(this.customerId, debitCardResponse.customerId) &&
        Objects.equals(this.cardNumber, debitCardResponse.cardNumber) &&
        Objects.equals(this.status, debitCardResponse.status) &&
        Objects.equals(this.createdAt, debitCardResponse.createdAt) &&
        Objects.equals(this.linkedAccounts, debitCardResponse.linkedAccounts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, customerId, cardNumber, status, createdAt, linkedAccounts);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DebitCardResponse {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    customerId: ").append(toIndentedString(customerId)).append("\n");
    sb.append("    cardNumber: ").append(toIndentedString(cardNumber)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    linkedAccounts: ").append(toIndentedString(linkedAccounts)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

