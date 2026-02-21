package com.springboot.project.generated.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.springboot.project.generated.model.TransactionDetailModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * TransactionFilterResponseModel
 */

@JsonTypeName("TransactionFilterResponse")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-21T21:19:19.577178617+07:00[Asia/Ho_Chi_Minh]", comments = "Generator version: 7.17.0")
public class TransactionFilterResponseModel implements Serializable {

  private static final long serialVersionUID = 1L;

  @Valid
  private List<@Valid TransactionDetailModel> data = new ArrayList<>();

  private @Nullable Long foundItems;

  private @Nullable Long totalItems;

  private @Nullable Long previousPageToken;

  private @Nullable Long nextPageToken;

  public TransactionFilterResponseModel data(List<@Valid TransactionDetailModel> data) {
    this.data = data;
    return this;
  }

  public TransactionFilterResponseModel addDataItem(TransactionDetailModel dataItem) {
    if (this.data == null) {
      this.data = new ArrayList<>();
    }
    this.data.add(dataItem);
    return this;
  }

  /**
   * Get data
   * @return data
   */
  @Valid 
  @Schema(name = "data", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("data")
  public List<@Valid TransactionDetailModel> getData() {
    return data;
  }

  public void setData(List<@Valid TransactionDetailModel> data) {
    this.data = data;
  }

  public TransactionFilterResponseModel foundItems(@Nullable Long foundItems) {
    this.foundItems = foundItems;
    return this;
  }

  /**
   * Get foundItems
   * @return foundItems
   */
  
  @Schema(name = "foundItems", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("foundItems")
  public @Nullable Long getFoundItems() {
    return foundItems;
  }

  public void setFoundItems(@Nullable Long foundItems) {
    this.foundItems = foundItems;
  }

  public TransactionFilterResponseModel totalItems(@Nullable Long totalItems) {
    this.totalItems = totalItems;
    return this;
  }

  /**
   * Get totalItems
   * @return totalItems
   */
  
  @Schema(name = "totalItems", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalItems")
  public @Nullable Long getTotalItems() {
    return totalItems;
  }

  public void setTotalItems(@Nullable Long totalItems) {
    this.totalItems = totalItems;
  }

  public TransactionFilterResponseModel previousPageToken(@Nullable Long previousPageToken) {
    this.previousPageToken = previousPageToken;
    return this;
  }

  /**
   * Get previousPageToken
   * @return previousPageToken
   */
  
  @Schema(name = "previousPageToken", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("previousPageToken")
  public @Nullable Long getPreviousPageToken() {
    return previousPageToken;
  }

  public void setPreviousPageToken(@Nullable Long previousPageToken) {
    this.previousPageToken = previousPageToken;
  }

  public TransactionFilterResponseModel nextPageToken(@Nullable Long nextPageToken) {
    this.nextPageToken = nextPageToken;
    return this;
  }

  /**
   * Get nextPageToken
   * @return nextPageToken
   */
  
  @Schema(name = "nextPageToken", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nextPageToken")
  public @Nullable Long getNextPageToken() {
    return nextPageToken;
  }

  public void setNextPageToken(@Nullable Long nextPageToken) {
    this.nextPageToken = nextPageToken;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TransactionFilterResponseModel transactionFilterResponse = (TransactionFilterResponseModel) o;
    return Objects.equals(this.data, transactionFilterResponse.data) &&
        Objects.equals(this.foundItems, transactionFilterResponse.foundItems) &&
        Objects.equals(this.totalItems, transactionFilterResponse.totalItems) &&
        Objects.equals(this.previousPageToken, transactionFilterResponse.previousPageToken) &&
        Objects.equals(this.nextPageToken, transactionFilterResponse.nextPageToken);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, foundItems, totalItems, previousPageToken, nextPageToken);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransactionFilterResponseModel {\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    foundItems: ").append(toIndentedString(foundItems)).append("\n");
    sb.append("    totalItems: ").append(toIndentedString(totalItems)).append("\n");
    sb.append("    previousPageToken: ").append(toIndentedString(previousPageToken)).append("\n");
    sb.append("    nextPageToken: ").append(toIndentedString(nextPageToken)).append("\n");
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

