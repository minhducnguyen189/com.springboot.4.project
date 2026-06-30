package com.springboot.project.common.validation;

import com.springboot.project.common.exception.BadRequestException;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public final class Validations {

  private static final String NULL_ERROR_MESSAGE = "The expected item for checking must not be null";
  private static final String BLANK_ERROR_MESSAGE = "The value must not be blank";
  private static final String EMAIL_ERROR_MESSAGE = "The value must be a valid email address";
  private static final String PHONE_ERROR_MESSAGE = "The value must be a valid phone number";
  private static final String IFSC_ERROR_MESSAGE = "The value must be a valid IFSC code";
  private static final String POSITIVE_ERROR_MESSAGE = "The value must be a positive number";
  private static final String NON_NEGATIVE_ERROR_MESSAGE = "The value must be a non-negative number";

  private static final Pattern EMAIL_PATTERN =
      Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
  private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{10,15}$");
  private static final Pattern IFSC_PATTERN = Pattern.compile("^[A-Z]{4}0[A-Z0-9]{6}$");

  public static <T> Validation<T> itemMustNotBeNull() {
    return t -> {
      if (Objects.isNull(t)) {
        throw new BadRequestException(NULL_ERROR_MESSAGE);
      }
    };
  }

  public static Validation<String> stringMustNotBeBlank() {
    return t -> {
      if (Objects.isNull(t) || t.isBlank()) {
        throw new BadRequestException(BLANK_ERROR_MESSAGE);
      }
    };
  }

  public static Validation<String> stringMustMatchEmailPattern() {
    return t -> {
      if (Objects.nonNull(t) && !t.isBlank() && !EMAIL_PATTERN.matcher(t).matches()) {
        throw new BadRequestException(EMAIL_ERROR_MESSAGE);
      }
    };
  }

  public static Validation<String> stringMustMatchPhonePattern() {
    return t -> {
      if (Objects.nonNull(t) && !t.isBlank() && !PHONE_PATTERN.matcher(t).matches()) {
        throw new BadRequestException(PHONE_ERROR_MESSAGE);
      }
    };
  }

  public static Validation<String> stringMustMatchIfscPattern() {
    return t -> {
      if (Objects.nonNull(t) && !t.isBlank() && !IFSC_PATTERN.matcher(t).matches()) {
        throw new BadRequestException(IFSC_ERROR_MESSAGE);
      }
    };
  }

  public static Validation<BigDecimal> numberMustBePositive() {
    return t -> {
      if (Objects.nonNull(t) && t.compareTo(BigDecimal.ZERO) <= 0) {
        throw new BadRequestException(POSITIVE_ERROR_MESSAGE);
      }
    };
  }

  public static Validation<BigDecimal> numberMustBeNonNegative() {
    return t -> {
      if (Objects.nonNull(t) && t.compareTo(BigDecimal.ZERO) < 0) {
        throw new BadRequestException(NON_NEGATIVE_ERROR_MESSAGE);
      }
    };
  }

  public static <T> Validation<T> itemMustSatisfy(Predicate<T> predicate, String errorMessage) {
    return t -> {
      if (Objects.nonNull(t) && !predicate.test(t)) {
        throw new BadRequestException(errorMessage);
      }
    };
  }
}