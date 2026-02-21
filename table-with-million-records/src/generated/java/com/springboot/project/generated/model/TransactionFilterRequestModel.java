package com.springboot.project.generated.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.springboot.project.generated.model.DomainEnumModel;
import com.springboot.project.generated.model.PaginationRequestModel;
import com.springboot.project.generated.model.PaymentMethodEnumModel;
import com.springboot.project.generated.model.TransactionStatusEnumModel;
import java.math.BigDecimal;
import java.time.LocalDate;
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
 * TransactionFilterRequestModel
 */

@JsonTypeName("TransactionFilterRequest")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-21T21:19:19.577178617+07:00[Asia/Ho_Chi_Minh]", comments = "Generator version: 7.17.0")
public class TransactionFilterRequestModel implements Serializable {

  private static final long serialVersionUID = 1L;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private @Nullable LocalDate date;

  private @Nullable DomainEnumModel domain;

  private @Nullable String location;

  private @Nullable BigDecimal value;

  private @Nullable TransactionStatusEnumModel status;

  private @Nullable PaymentMethodEnumModel paymentMethod;

  private @Nullable BigDecimal taxAmount;

  private @Nullable BigDecimal netValue;

  private @Nullable PaginationRequestModel pagination;

  public TransactionFilterRequestModel date(@Nullable LocalDate date) {
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

  public TransactionFilterRequestModel domain(@Nullable DomainEnumModel domain) {
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

  public TransactionFilterRequestModel location(@Nullable String location) {
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

  public TransactionFilterRequestModel value(@Nullable BigDecimal value) {
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

  public TransactionFilterRequestModel status(@Nullable TransactionStatusEnumModel status) {
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

  public TransactionFilterRequestModel paymentMethod(@Nullable PaymentMethodEnumModel paymentMethod) {
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

  public TransactionFilterRequestModel taxAmount(@Nullable BigDecimal taxAmount) {
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

  public TransactionFilterRequestModel netValue(@Nullable BigDecimal netValue) {
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

  public TransactionFilterRequestModel pagination(@Nullable PaginationRequestModel pagination) {
    this.pagination = pagination;
    return this;
  }

  /**
   * Get pagination
   * @return pagination
   */
  @Valid 
  @Schema(name = "pagination", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pagination")
  public @Nullable PaginationRequestModel getPagination() {
    return pagination;
  }

  public void setPagination(@Nullable PaginationRequestModel pagination) {
    this.pagination = pagination;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TransactionFilterRequestModel transactionFilterRequest = (TransactionFilterRequestModel) o;
    return Objects.equals(this.date, transactionFilterRequest.date) &&
        Objects.equals(this.domain, transactionFilterRequest.domain) &&
        Objects.equals(this.location, transactionFilterRequest.location) &&
        Objects.equals(this.value, transactionFilterRequest.value) &&
        Objects.equals(this.status, transactionFilterRequest.status) &&
        Objects.equals(this.paymentMethod, transactionFilterRequest.paymentMethod) &&
        Objects.equals(this.taxAmount, transactionFilterRequest.taxAmount) &&
        Objects.equals(this.netValue, transactionFilterRequest.netValue) &&
        Objects.equals(this.pagination, transactionFilterRequest.pagination);
  }

  @Override
  public int hashCode() {
    return Objects.hash(date, domain, location, value, status, paymentMethod, taxAmount, netValue, pagination);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransactionFilterRequestModel {\n");
    sb.append("    date: ").append(toIndentedString(date)).append("\n");
    sb.append("    domain: ").append(toIndentedString(domain)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    paymentMethod: ").append(toIndentedString(paymentMethod)).append("\n");
    sb.append("    taxAmount: ").append(toIndentedString(taxAmount)).append("\n");
    sb.append("    netValue: ").append(toIndentedString(netValue)).append("\n");
    sb.append("    pagination: ").append(toIndentedString(pagination)).append("\n");
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

