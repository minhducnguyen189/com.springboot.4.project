package com.springboot.project.generated.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.springboot.project.generated.model.AccountStatusEnumModel;
import com.springboot.project.generated.model.AccountTypeEnumModel;
import com.springboot.project.generated.model.PaginationRequestModel;
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
 * BankAccountFilterRequestModel
 */

@JsonTypeName("BankAccountFilterRequest")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-21T21:19:19.577178617+07:00[Asia/Ho_Chi_Minh]", comments = "Generator version: 7.17.0")
public class BankAccountFilterRequestModel implements Serializable {

  private static final long serialVersionUID = 1L;

  private @Nullable String firstName;

  private @Nullable String lastName;

  private @Nullable String phone;

  private @Nullable String email;

  private @Nullable String accountNumber;

  private @Nullable AccountTypeEnumModel accountType;

  private @Nullable String ifscCode;

  private @Nullable AccountStatusEnumModel status;

  private @Nullable PaginationRequestModel pagination;

  public BankAccountFilterRequestModel firstName(@Nullable String firstName) {
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

  public BankAccountFilterRequestModel lastName(@Nullable String lastName) {
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

  public BankAccountFilterRequestModel phone(@Nullable String phone) {
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

  public BankAccountFilterRequestModel email(@Nullable String email) {
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

  public BankAccountFilterRequestModel accountNumber(@Nullable String accountNumber) {
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

  public BankAccountFilterRequestModel accountType(@Nullable AccountTypeEnumModel accountType) {
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

  public BankAccountFilterRequestModel ifscCode(@Nullable String ifscCode) {
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

  public BankAccountFilterRequestModel status(@Nullable AccountStatusEnumModel status) {
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

  public BankAccountFilterRequestModel pagination(@Nullable PaginationRequestModel pagination) {
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
    BankAccountFilterRequestModel bankAccountFilterRequest = (BankAccountFilterRequestModel) o;
    return Objects.equals(this.firstName, bankAccountFilterRequest.firstName) &&
        Objects.equals(this.lastName, bankAccountFilterRequest.lastName) &&
        Objects.equals(this.phone, bankAccountFilterRequest.phone) &&
        Objects.equals(this.email, bankAccountFilterRequest.email) &&
        Objects.equals(this.accountNumber, bankAccountFilterRequest.accountNumber) &&
        Objects.equals(this.accountType, bankAccountFilterRequest.accountType) &&
        Objects.equals(this.ifscCode, bankAccountFilterRequest.ifscCode) &&
        Objects.equals(this.status, bankAccountFilterRequest.status) &&
        Objects.equals(this.pagination, bankAccountFilterRequest.pagination);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstName, lastName, phone, email, accountNumber, accountType, ifscCode, status, pagination);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BankAccountFilterRequestModel {\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    accountNumber: ").append(toIndentedString(accountNumber)).append("\n");
    sb.append("    accountType: ").append(toIndentedString(accountType)).append("\n");
    sb.append("    ifscCode: ").append(toIndentedString(ifscCode)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

