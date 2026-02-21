package com.springboot.project.generated.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets AccountTypeEnum
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-21T21:19:19.577178617+07:00[Asia/Ho_Chi_Minh]", comments = "Generator version: 7.17.0")
public enum AccountTypeEnumModel implements Serializable {
  
  SAVINGS("SAVINGS"),
  
  CURRENT("CURRENT");

  private final String value;

  AccountTypeEnumModel(String value) {
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
  public static AccountTypeEnumModel fromValue(String value) {
    for (AccountTypeEnumModel b : AccountTypeEnumModel.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

