package com.springboot.project.service.common.functions;

import com.springboot.project.exception.BadRequestException;

import java.util.Objects;

public final class Validations {

  private static final String NULL_ERROR_MESSAGE = "The expected item for checking must not be null";

  public static <T> Validation<T> itemMustNotBeNull() {
    return t -> {
      if (Objects.isNull(t)) {
        throw new BadRequestException(NULL_ERROR_MESSAGE);
      }
    };
  }



}
