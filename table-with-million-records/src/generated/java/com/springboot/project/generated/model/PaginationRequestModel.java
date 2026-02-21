package com.springboot.project.generated.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.springboot.project.generated.model.SortOrderEnumModel;
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
 * PaginationRequestModel
 */

@JsonTypeName("PaginationRequest")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-21T21:19:19.577178617+07:00[Asia/Ho_Chi_Minh]", comments = "Generator version: 7.17.0")
public class PaginationRequestModel implements Serializable {

  private static final long serialVersionUID = 1L;

  private @Nullable Integer pageSize;

  private @Nullable Integer pageNumber;

  private @Nullable String sortBy;

  private @Nullable SortOrderEnumModel sortOrder;

  private @Nullable Long previousPageToken;

  private @Nullable Long nextPageToken;

  public PaginationRequestModel pageSize(@Nullable Integer pageSize) {
    this.pageSize = pageSize;
    return this;
  }

  /**
   * Get pageSize
   * @return pageSize
   */
  
  @Schema(name = "pageSize", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pageSize")
  public @Nullable Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(@Nullable Integer pageSize) {
    this.pageSize = pageSize;
  }

  public PaginationRequestModel pageNumber(@Nullable Integer pageNumber) {
    this.pageNumber = pageNumber;
    return this;
  }

  /**
   * Get pageNumber
   * @return pageNumber
   */
  
  @Schema(name = "pageNumber", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pageNumber")
  public @Nullable Integer getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(@Nullable Integer pageNumber) {
    this.pageNumber = pageNumber;
  }

  public PaginationRequestModel sortBy(@Nullable String sortBy) {
    this.sortBy = sortBy;
    return this;
  }

  /**
   * Get sortBy
   * @return sortBy
   */
  
  @Schema(name = "sortBy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sortBy")
  public @Nullable String getSortBy() {
    return sortBy;
  }

  public void setSortBy(@Nullable String sortBy) {
    this.sortBy = sortBy;
  }

  public PaginationRequestModel sortOrder(@Nullable SortOrderEnumModel sortOrder) {
    this.sortOrder = sortOrder;
    return this;
  }

  /**
   * Get sortOrder
   * @return sortOrder
   */
  @Valid 
  @Schema(name = "sortOrder", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sortOrder")
  public @Nullable SortOrderEnumModel getSortOrder() {
    return sortOrder;
  }

  public void setSortOrder(@Nullable SortOrderEnumModel sortOrder) {
    this.sortOrder = sortOrder;
  }

  public PaginationRequestModel previousPageToken(@Nullable Long previousPageToken) {
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

  public PaginationRequestModel nextPageToken(@Nullable Long nextPageToken) {
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
    PaginationRequestModel paginationRequest = (PaginationRequestModel) o;
    return Objects.equals(this.pageSize, paginationRequest.pageSize) &&
        Objects.equals(this.pageNumber, paginationRequest.pageNumber) &&
        Objects.equals(this.sortBy, paginationRequest.sortBy) &&
        Objects.equals(this.sortOrder, paginationRequest.sortOrder) &&
        Objects.equals(this.previousPageToken, paginationRequest.previousPageToken) &&
        Objects.equals(this.nextPageToken, paginationRequest.nextPageToken);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pageSize, pageNumber, sortBy, sortOrder, previousPageToken, nextPageToken);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PaginationRequestModel {\n");
    sb.append("    pageSize: ").append(toIndentedString(pageSize)).append("\n");
    sb.append("    pageNumber: ").append(toIndentedString(pageNumber)).append("\n");
    sb.append("    sortBy: ").append(toIndentedString(sortBy)).append("\n");
    sb.append("    sortOrder: ").append(toIndentedString(sortOrder)).append("\n");
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

