package com.springboot.project.generated.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.springboot.project.generated.model.AccountStatusEnumModel;
import com.springboot.project.generated.model.AccountTypeEnumModel;
import com.springboot.project.generated.model.CurrencyEnumModel;
import java.math.BigDecimal;
import java.util.UUID;
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
 * BankAccountDetailModel
 */

@JsonTypeName("BankAccountDetail")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-21T21:19:19.577178617+07:00[Asia/Ho_Chi_Minh]", comments = "Generator version: 7.17.0")
public class BankAccountDetailModel implements Serializable {

  private static final long serialVersionUID = 1L;

  private @Nullable UUID id;

  private @Nullable Long sequenceNumber;

  private @Nullable String firstName;

  private @Nullable String lastName;

  private @Nullable String phone;

  private @Nullable String email;

  private @Nullable String street;

  private @Nullable String streetNumber;

  private @Nullable String postalCode;

  private @Nullable String city;

  private @Nullable String country;

  private @Nullable String accountNumber;

  private @Nullable AccountTypeEnumModel accountType;

  private @Nullable String ifscCode;

  private @Nullable BigDecimal balance;

  private @Nullable CurrencyEnumModel currency;

  private @Nullable AccountStatusEnumModel status;

  public BankAccountDetailModel id(@Nullable UUID id) {
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

  public BankAccountDetailModel sequenceNumber(@Nullable Long sequenceNumber) {
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

  public BankAccountDetailModel firstName(@Nullable String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * Get firstName
   * @return firstName
   */
  
  @Schema(name = "firstName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("firstName")
  public @Nullable String getFirstName() {
    return firstName;
  }

  public void setFirstName(@Nullable String firstName) {
    this.firstName = firstName;
  }

  public BankAccountDetailModel lastName(@Nullable String lastName) {
    this.lastName = lastName;
    return this;
  }

  /**
   * Get lastName
   * @return lastName
   */
  
  @Schema(name = "lastName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastName")
  public @Nullable String getLastName() {
    return lastName;
  }

  public void setLastName(@Nullable String lastName) {
    this.lastName = lastName;
  }

  public BankAccountDetailModel phone(@Nullable String phone) {
    this.phone = phone;
    return this;
  }

  /**
   * Get phone
   * @return phone
   */
  
  @Schema(name = "phone", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("phone")
  public @Nullable String getPhone() {
    return phone;
  }

  public void setPhone(@Nullable String phone) {
    this.phone = phone;
  }

  public BankAccountDetailModel email(@Nullable String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
   */
  
  @Schema(name = "email", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("email")
  public @Nullable String getEmail() {
    return email;
  }

  public void setEmail(@Nullable String email) {
    this.email = email;
  }

  public BankAccountDetailModel street(@Nullable String street) {
    this.street = street;
    return this;
  }

  /**
   * Get street
   * @return street
   */
  
  @Schema(name = "street", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("street")
  public @Nullable String getStreet() {
    return street;
  }

  public void setStreet(@Nullable String street) {
    this.street = street;
  }

  public BankAccountDetailModel streetNumber(@Nullable String streetNumber) {
    this.streetNumber = streetNumber;
    return this;
  }

  /**
   * Get streetNumber
   * @return streetNumber
   */
  
  @Schema(name = "streetNumber", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("streetNumber")
  public @Nullable String getStreetNumber() {
    return streetNumber;
  }

  public void setStreetNumber(@Nullable String streetNumber) {
    this.streetNumber = streetNumber;
  }

  public BankAccountDetailModel postalCode(@Nullable String postalCode) {
    this.postalCode = postalCode;
    return this;
  }

  /**
   * Get postalCode
   * @return postalCode
   */
  
  @Schema(name = "postalCode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("postalCode")
  public @Nullable String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(@Nullable String postalCode) {
    this.postalCode = postalCode;
  }

  public BankAccountDetailModel city(@Nullable String city) {
    this.city = city;
    return this;
  }

  /**
   * Get city
   * @return city
   */
  
  @Schema(name = "city", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("city")
  public @Nullable String getCity() {
    return city;
  }

  public void setCity(@Nullable String city) {
    this.city = city;
  }

  public BankAccountDetailModel country(@Nullable String country) {
    this.country = country;
    return this;
  }

  /**
   * Get country
   * @return country
   */
  
  @Schema(name = "country", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("country")
  public @Nullable String getCountry() {
    return country;
  }

  public void setCountry(@Nullable String country) {
    this.country = country;
  }

  public BankAccountDetailModel accountNumber(@Nullable String accountNumber) {
    this.accountNumber = accountNumber;
    return this;
  }

  /**
   * Get accountNumber
   * @return accountNumber
   */
  
  @Schema(name = "accountNumber", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("accountNumber")
  public @Nullable String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(@Nullable String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public BankAccountDetailModel accountType(@Nullable AccountTypeEnumModel accountType) {
    this.accountType = accountType;
    return this;
  }

  /**
   * Get accountType
   * @return accountType
   */
  @Valid 
  @Schema(name = "accountType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("accountType")
  public @Nullable AccountTypeEnumModel getAccountType() {
    return accountType;
  }

  public void setAccountType(@Nullable AccountTypeEnumModel accountType) {
    this.accountType = accountType;
  }

  public BankAccountDetailModel ifscCode(@Nullable String ifscCode) {
    this.ifscCode = ifscCode;
    return this;
  }

  /**
   * Get ifscCode
   * @return ifscCode
   */
  
  @Schema(name = "ifscCode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ifscCode")
  public @Nullable String getIfscCode() {
    return ifscCode;
  }

  public void setIfscCode(@Nullable String ifscCode) {
    this.ifscCode = ifscCode;
  }

  public BankAccountDetailModel balance(@Nullable BigDecimal balance) {
    this.balance = balance;
    return this;
  }

  /**
   * Get balance
   * @return balance
   */
  @Valid 
  @Schema(name = "balance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("balance")
  public @Nullable BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(@Nullable BigDecimal balance) {
    this.balance = balance;
  }

  public BankAccountDetailModel currency(@Nullable CurrencyEnumModel currency) {
    this.currency = currency;
    return this;
  }

  /**
   * Get currency
   * @return currency
   */
  @Valid 
  @Schema(name = "currency", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currency")
  public @Nullable CurrencyEnumModel getCurrency() {
    return currency;
  }

  public void setCurrency(@Nullable CurrencyEnumModel currency) {
    this.currency = currency;
  }

  public BankAccountDetailModel status(@Nullable AccountStatusEnumModel status) {
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
  public @Nullable AccountStatusEnumModel getStatus() {
    return status;
  }

  public void setStatus(@Nullable AccountStatusEnumModel status) {
    this.status = status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BankAccountDetailModel bankAccountDetail = (BankAccountDetailModel) o;
    return Objects.equals(this.id, bankAccountDetail.id) &&
        Objects.equals(this.sequenceNumber, bankAccountDetail.sequenceNumber) &&
        Objects.equals(this.firstName, bankAccountDetail.firstName) &&
        Objects.equals(this.lastName, bankAccountDetail.lastName) &&
        Objects.equals(this.phone, bankAccountDetail.phone) &&
        Objects.equals(this.email, bankAccountDetail.email) &&
        Objects.equals(this.street, bankAccountDetail.street) &&
        Objects.equals(this.streetNumber, bankAccountDetail.streetNumber) &&
        Objects.equals(this.postalCode, bankAccountDetail.postalCode) &&
        Objects.equals(this.city, bankAccountDetail.city) &&
        Objects.equals(this.country, bankAccountDetail.country) &&
        Objects.equals(this.accountNumber, bankAccountDetail.accountNumber) &&
        Objects.equals(this.accountType, bankAccountDetail.accountType) &&
        Objects.equals(this.ifscCode, bankAccountDetail.ifscCode) &&
        Objects.equals(this.balance, bankAccountDetail.balance) &&
        Objects.equals(this.currency, bankAccountDetail.currency) &&
        Objects.equals(this.status, bankAccountDetail.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, sequenceNumber, firstName, lastName, phone, email, street, streetNumber, postalCode, city, country, accountNumber, accountType, ifscCode, balance, currency, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BankAccountDetailModel {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    sequenceNumber: ").append(toIndentedString(sequenceNumber)).append("\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    street: ").append(toIndentedString(street)).append("\n");
    sb.append("    streetNumber: ").append(toIndentedString(streetNumber)).append("\n");
    sb.append("    postalCode: ").append(toIndentedString(postalCode)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    country: ").append(toIndentedString(country)).append("\n");
    sb.append("    accountNumber: ").append(toIndentedString(accountNumber)).append("\n");
    sb.append("    accountType: ").append(toIndentedString(accountType)).append("\n");
    sb.append("    ifscCode: ").append(toIndentedString(ifscCode)).append("\n");
    sb.append("    balance: ").append(toIndentedString(balance)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

