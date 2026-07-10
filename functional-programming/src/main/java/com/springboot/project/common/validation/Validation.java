package com.springboot.project.common.validation;

import java.util.Objects;
import java.util.function.Consumer;

@FunctionalInterface
public interface Validation<T> {

  void accept(T t);

  /**
   * Returns a composed {@code Consumer} that performs, in sequence, this
   * operation followed by the {@code after} operation. If performing either
   * operation throws an exception, it is relayed to the caller of the
   * composed operation.  If performing this operation throws an exception,
   * the {@code after} operation will not be performed.
   *
   * @param after the operation to perform after this operation
   * @return a composed {@code Consumer} that performs in sequence this
   * operation followed by the {@code after} operation
   * @throws NullPointerException if {@code after} is null
   */
  default Validation<T> andThen(Validation<? super T> after) {
    Objects.requireNonNull(after);
    return (T t) -> {
      accept(t);
      after.accept(t);
    };
  }

  default Validation<T> andField(T value) {
    this.accept(value);
    return this;
  }


}
