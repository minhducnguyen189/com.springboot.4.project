package com.springboot.project.service.bank_account.impl;

import com.springboot.project.exception.BadRequestException;
import com.springboot.project.service.bank_account.model.BankAccountFilterRequestModel;
import com.springboot.project.service.common.functions.Validations;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BankAccountServiceTest {

  private static final String NULL_ERROR_MESSAGE = "The expected item for checking must not be null";

  @Test
  void validate_function_test() {
    BankAccountFilterRequestModel filterRequest = new BankAccountFilterRequestModel();
    filterRequest.setAccountNumber("AAAAA");
    filterRequest.setEmail("abc@gmail.com");
    filterRequest.setPhone("+84123123123");
    filterRequest.setFirstName("FirstName");

    assertThatThrownBy(() -> { Validations.itemMustNotBeNull()
            .doTheSameWithField(filterRequest.getEmail())
            .doTheSameWithField(filterRequest.getPhone())
            .doTheSameWithField(filterRequest.getLastName())
            .doTheSameWithField(filterRequest.getFirstName())
            .doTheSameWithField(filterRequest.getIfscCode())
            .accept(filterRequest.getAccountNumber());
    }).isInstanceOf(BadRequestException.class)
            .hasMessage(NULL_ERROR_MESSAGE);
  }
}
