package com.springboot.project.bankaccount.validation;

import com.springboot.project.common.exception.BadRequestException;
import com.springboot.project.bankaccount.model.BankAccountFilterRequestModel;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public final class BankAccountFilterRequestValidation {

  private static final String NULL_ERROR_MESSAGE = "The expected item for checking must not be null";
  private static final String BLANK_ERROR_MESSAGE = "The value must not be blank";
  private static final String PHONE_ERROR_MESSAGE = "The value must be a valid phone number";
  private static final String EMAIL_ERROR_MESSAGE = "The value must be a valid email address";

  private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{10,15}$");
  private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

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
      throw new BadRequestException(NULL_ERROR_MESSAGE);
    }
    if (request.getFirstName() == null) {
      throw new BadRequestException(NULL_ERROR_MESSAGE);
    }
  }

  private static void validateStringFields(BankAccountFilterRequestModel request) {
    if (request.getLastName() != null && request.getLastName().isBlank()) {
      throw new BadRequestException(BLANK_ERROR_MESSAGE);
    }
    if (request.getAccountNumber() != null && request.getAccountNumber().isBlank()) {
      throw new BadRequestException(BLANK_ERROR_MESSAGE);
    }
    if (request.getIfscCode() != null && request.getIfscCode().isBlank()) {
      throw new BadRequestException(BLANK_ERROR_MESSAGE);
    }
  }

  private static void validatePhonePattern(BankAccountFilterRequestModel request) {
    if (request.getPhone() != null && !request.getPhone().isBlank()) {
      if (!PHONE_PATTERN.matcher(request.getPhone()).matches()) {
        throw new BadRequestException(PHONE_ERROR_MESSAGE);
      }
    }
  }

  private static void validateEmailPattern(BankAccountFilterRequestModel request) {
    if (request.getEmail() != null && !request.getEmail().isBlank()) {
      if (!EMAIL_PATTERN.matcher(request.getEmail()).matches()) {
        throw new BadRequestException(EMAIL_ERROR_MESSAGE);
      }
    }
  }
}