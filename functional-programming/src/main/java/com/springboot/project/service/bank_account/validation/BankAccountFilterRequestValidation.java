package com.springboot.project.service.bank_account.validation;

import com.springboot.project.exception.BadRequestException;
import com.springboot.project.service.bank_account.model.BankAccountFilterRequestModel;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class BankAccountFilterRequestValidation {

  private BankAccountFilterRequestValidation() {}

  public static Consumer<BankAccountFilterRequestModel> validate() {
    return request -> {
      validatePagination(request);
      validateStringFields(request);
      validatePhonePattern(request);
      validateEmailPattern(request);
    };
  }

  private static void validatePagination(BankAccountFilterRequestModel request) {
    if (request.getPagination() == null) {
      throw new BadRequestException("The expected item for checking must not be null");
    }
    if (request.getFirstName() == null) {
      throw new BadRequestException("The expected item for checking must not be null");
    }
  }

  private static void validateStringFields(BankAccountFilterRequestModel request) {
    if (request.getLastName() != null && request.getLastName().isBlank()) {
      throw new BadRequestException("The value must not be blank");
    }
    if (request.getAccountNumber() != null && request.getAccountNumber().isBlank()) {
      throw new BadRequestException("The value must not be blank");
    }
    if (request.getIfscCode() != null && request.getIfscCode().isBlank()) {
      throw new BadRequestException("The value must not be blank");
    }
  }

  private static void validatePhonePattern(BankAccountFilterRequestModel request) {
    if (request.getPhone() != null && !request.getPhone().isBlank()) {
      if (!request.getPhone().matches("^\\+?[0-9]{10,15}$")) {
        throw new BadRequestException("The value must be a valid phone number");
      }
    }
  }

  private static void validateEmailPattern(BankAccountFilterRequestModel request) {
    if (request.getEmail() != null && !request.getEmail().isBlank()) {
      if (!request.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
        throw new BadRequestException("The value must be a valid email address");
      }
    }
  }
}