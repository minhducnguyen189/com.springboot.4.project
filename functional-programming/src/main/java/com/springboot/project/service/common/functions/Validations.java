package com.springboot.project.service.common.functions;

import com.springboot.project.exception.BadRequestException;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public final class Validations {

  private static final String NULL_ERROR_MESSAGE = "The expected item for checking must not be null";
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
        throw new BadRequestException("The value must not be blank");
      }
    };
  }

  public static Validation<String> stringMustMatchEmailPattern() {
    return t -> {
      if (Objects.nonNull(t) && !t.isBlank() && !EMAIL_PATTERN.matcher(t).matches()) {
        throw new BadRequestException("The value must be a valid email address");
      }
    };
  }

  public static Validation<String> stringMustMatchPhonePattern() {
    return t -> {
      if (Objects.nonNull(t) && !t.isBlank() && !PHONE_PATTERN.matcher(t).matches()) {
        throw new BadRequestException("The value must be a valid phone number");
      }
    };
  }

  public static Validation<String> stringMustMatchIfscPattern() {
    return t -> {
      if (Objects.nonNull(t) && !t.isBlank() && !IFSC_PATTERN.matcher(t).matches()) {
        throw new BadRequestException("The value must be a valid IFSC code");
      }
    };
  }

  public static Validation<BigDecimal> numberMustBePositive() {
    return t -> {
      if (Objects.nonNull(t) && t.compareTo(BigDecimal.ZERO) <= 0) {
        throw new BadRequestException("The value must be a positive number");
      }
    };
  }

  public static Validation<BigDecimal> numberMustBeNonNegative() {
    return t -> {
      if (Objects.nonNull(t) && t.compareTo(BigDecimal.ZERO) < 0) {
        throw new BadRequestException("The value must be a non-negative number");
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
