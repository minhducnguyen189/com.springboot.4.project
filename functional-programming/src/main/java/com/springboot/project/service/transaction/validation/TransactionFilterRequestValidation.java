package com.springboot.project.service.transaction.validation;

import com.springboot.project.exception.BadRequestException;
import com.springboot.project.service.transaction.model.TransactionFilterRequestModel;
import java.math.BigDecimal;
import java.util.function.Consumer;

public final class TransactionFilterRequestValidation {

  private TransactionFilterRequestValidation() {}

  public static Consumer<TransactionFilterRequestModel> validate() {
    return request -> {
      validatePagination(request);
      validateNumbers(request);
    };
  }

  private static void validatePagination(TransactionFilterRequestModel request) {
    if (request.getPagination() == null) {
      throw new BadRequestException("The expected item for checking must not be null");
    }
    if (request.getDate() == null) {
      throw new BadRequestException("The expected item for checking must not be null");
    }
  }

  private static void validateNumbers(TransactionFilterRequestModel request) {
    if (request.getValue() != null && request.getValue().compareTo(BigDecimal.ZERO) < 0) {
      throw new BadRequestException("The value must be a non-negative number");
    }
    if (request.getTaxAmount() != null && request.getTaxAmount().compareTo(BigDecimal.ZERO) < 0) {
      throw new BadRequestException("The value must be a non-negative number");
    }
    if (request.getNetValue() != null && request.getNetValue().compareTo(BigDecimal.ZERO) < 0) {
      throw new BadRequestException("The value must be a non-negative number");
    }
  }
}