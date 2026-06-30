package com.springboot.project.transaction.validation;

import com.springboot.project.common.exception.BadRequestException;
import com.springboot.project.transaction.model.TransactionFilterRequestModel;
import java.math.BigDecimal;
import java.util.function.Consumer;

public final class TransactionFilterRequestValidation {

  private static final String NULL_ERROR_MESSAGE = "The expected item for checking must not be null";
  private static final String NON_NEGATIVE_ERROR_MESSAGE = "The value must be a non-negative number";

  private TransactionFilterRequestValidation() {}

  public static Consumer<TransactionFilterRequestModel> validate() {
    return request -> {
      validatePagination(request);
      validateNumbers(request);
    };
  }

  private static void validatePagination(TransactionFilterRequestModel request) {
    if (request.getPagination() == null) {
      throw new BadRequestException(NULL_ERROR_MESSAGE);
    }
    if (request.getDate() == null) {
      throw new BadRequestException(NULL_ERROR_MESSAGE);
    }
  }

  private static void validateNumbers(TransactionFilterRequestModel request) {
    if (request.getValue() != null && request.getValue().compareTo(BigDecimal.ZERO) < 0) {
      throw new BadRequestException(NON_NEGATIVE_ERROR_MESSAGE);
    }
    if (request.getTaxAmount() != null && request.getTaxAmount().compareTo(BigDecimal.ZERO) < 0) {
      throw new BadRequestException(NON_NEGATIVE_ERROR_MESSAGE);
    }
    if (request.getNetValue() != null && request.getNetValue().compareTo(BigDecimal.ZERO) < 0) {
      throw new BadRequestException(NON_NEGATIVE_ERROR_MESSAGE);
    }
  }
}