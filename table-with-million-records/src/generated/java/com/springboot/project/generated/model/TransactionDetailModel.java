package com.springboot.project.generated.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.springboot.project.generated.model.DomainEnumModel;
import com.springboot.project.generated.model.PaymentMethodEnumModel;
import com.springboot.project.generated.model.TransactionStatusEnumModel;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TransactionDetailModel
 */

@JsonTypeName("TransactionDetail")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-21T21:19:19.577178617+07:00[Asia/Ho_Chi_Minh]", comments = "Generator version: 7.17.0")
public class TransactionDetailModel implements Serializable {

  private static final long serialVersionUID = 1L;

  private @Nullable UUID id;

  private @Nullable String transactionNumber;

  private @Nullable Long sequenceNumber;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private @Nullable LocalDate date;

  private @Nullable DomainEnumModel domain;

  private @Nullable String location;

  private @Nullable BigDecimal value;

  private @Nullable TransactionStatusEnumModel status;

  private @Nullable PaymentMethodEnumModel paymentMethod;

  private @Nullable BigDecimal taxAmount;

  private @Nullable BigDecimal netValue;

  public TransactionDetailModel id(@Nullable UUID id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  @Valid 
  @Schema(name = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public @Nullable UUID getId() {
    return id;
  }

  public void setId(@Nullable UUID id) {
    this.id = id;
  }

  public TransactionDetailModel transactionNumber(@Nullable String transactionNumber) {
    this.transactionNumber = transactionNumber;
    return this;
  }

  /**
   * Get transactionNumber
   * @return transactionNumber
   */
  
  @Schema(name = "transactionNumber", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("transactionNumber")
  public @Nullable String getTransactionNumber() {
    return transactionNumber;
  }

  public void setTransactionNumber(@Nullable String transactionNumber) {
    this.transactionNumber = transactionNumber;
  }

  public TransactionDetailModel sequenceNumber(@Nullable Long sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
    return this;
  }

  /**
   * Get sequenceNumber
   * @return sequenceNumber
   */
  
  @Schema(name = "sequenceNumber", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sequenceNumber")
  public @Nullable Long getSequenceNumber() {
    return sequenceNumber;
  }

  public void setSequenceNumber(@Nullable Long sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }

  public TransactionDetailModel date(@Nullable LocalDate date) {
    this.date = date;
    return this;
  }

  /**
   * Get date
   * @return date
   */
  @Valid 
  @Schema(name = "date", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("date")
  public @Nullable LocalDate getDate() {
    return date;
  }

  public void setDate(@Nullable LocalDate date) {
    this.date = date;
  }

  public TransactionDetailModel domain(@Nullable DomainEnumModel domain) {
    this.domain = domain;
    return this;
  }

  /**
   * Get domain
   * @return domain
   */
  @Valid 
  @Schema(name = "domain", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("domain")
  public @Nullable DomainEnumModel getDomain() {
    return domain;
  }

  public void setDomain(@Nullable DomainEnumModel domain) {
    this.domain = domain;
  }

  public TransactionDetailModel location(@Nullable String location) {
    this.location = location;
    return this;
  }

  /**
   * Get location
   * @return location
   */
  
  @Schema(name = "location", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("location")
  public @Nullable String getLocation() {
    return location;
  }

  public void setLocation(@Nullable String location) {
    this.location = location;
  }

  public TransactionDetailModel value(@Nullable BigDecimal value) {
    this.value = value;
    return this;
  }

  /**
   * Get value
   * @return value
   */
  @Valid 
  @Schema(name = "value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("value")
  public @Nullable BigDecimal getValue() {
    return value;
  }

  public void setValue(@Nullable BigDecimal value) {
    this.value = value;
  }

  public TransactionDetailModel status(@Nullable TransactionStatusEnumModel status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @Valid 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable TransactionStatusEnumModel getStatus() {
    return status;
  }

  public void setStatus(@Nullable TransactionStatusEnumModel status) {
    this.status = status;
  }

  public TransactionDetailModel paymentMethod(@Nullable PaymentMethodEnumModel paymentMethod) {
    this.paymentMethod = paymentMethod;
    return this;
  }

  /**
   * Get paymentMethod
   * @return paymentMethod
   */
  @Valid 
  @Schema(name = "paymentMethod", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("paymentMethod")
  public @Nullable PaymentMethodEnumModel getPaymentMethod() {
    return paymentMethod;
  }

  public void setPaymentMethod(@Nullable PaymentMethodEnumModel paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  public TransactionDetailModel taxAmount(@Nullable BigDecimal taxAmount) {
    this.taxAmount = taxAmount;
    return this;
  }

  /**
   * Get taxAmount
   * @return taxAmount
   */
  @Valid 
  @Schema(name = "taxAmount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("taxAmount")
  public @Nullable BigDecimal getTaxAmount() {
    return taxAmount;
  }

  public void setTaxAmount(@Nullable BigDecimal taxAmount) {
    this.taxAmount = taxAmount;
  }

  public TransactionDetailModel netValue(@Nullable BigDecimal netValue) {
    this.netValue = netValue;
    return this;
  }

  /**
   * Get netValue
   * @return netValue
   */
  @Valid 
  @Schema(name = "netValue", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("netValue")
  public @Nullable BigDecimal getNetValue() {
    return netValue;
  }

  public void setNetValue(@Nullable BigDecimal netValue) {
    this.netValue = netValue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TransactionDetailModel transactionDetail = (TransactionDetailModel) o;
    return Objects.equals(this.id, transactionDetail.id) &&
        Objects.equals(this.transactionNumber, transactionDetail.transactionNumber) &&
        Objects.equals(this.sequenceNumber, transactionDetail.sequenceNumber) &&
        Objects.equals(this.date, transactionDetail.date) &&
        Objects.equals(this.domain, transactionDetail.domain) &&
        Objects.equals(this.location, transactionDetail.location) &&
        Objects.equals(this.value, transactionDetail.value) &&
        Objects.equals(this.status, transactionDetail.status) &&
        Objects.equals(this.paymentMethod, transactionDetail.paymentMethod) &&
        Objects.equals(this.taxAmount, transactionDetail.taxAmount) &&
        Objects.equals(this.netValue, transactionDetail.netValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, transactionNumber, sequenceNumber, date, domain, location, value, status, paymentMethod, taxAmount, netValue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransactionDetailModel {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    transactionNumber: ").append(toIndentedString(transactionNumber)).append("\n");
    sb.append("    sequenceNumber: ").append(toIndentedString(sequenceNumber)).append("\n");
    sb.append("    date: ").append(toIndentedString(date)).append("\n");
    sb.append("    domain: ").append(toIndentedString(domain)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    paymentMethod: ").append(toIndentedString(paymentMethod)).append("\n");
    sb.append("    taxAmount: ").append(toIndentedString(taxAmount)).append("\n");
    sb.append("    netValue: ").append(toIndentedString(netValue)).append("\n");
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

