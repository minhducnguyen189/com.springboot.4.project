package com.springboot.project.bankaccount.service;

import com.springboot.project.common.exception.BadRequestException;
import com.springboot.project.bankaccount.model.BankAccountFilterRequestModel;
import com.springboot.project.common.validation.Validations;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BankAccountServiceTest {

  private static final String NULL_ERROR_MESSAGE = "The expected item for checking must not be null";

  @Test
  void should_throwBadRequest_when_aChainedFieldIsNull() {
    BankAccountFilterRequestModel filterRequest = new BankAccountFilterRequestModel();
    filterRequest.setAccountNumber("AAAAA");
    filterRequest.setEmail("abc@gmail.com");
    filterRequest.setPhone("+84123123123");
    filterRequest.setFirstName("FirstName");

    assertThatThrownBy(() -> { Validations.itemMustNotBeNull()
            .andField(filterRequest.getEmail())
            .andField(filterRequest.getPhone())
            .andField(filterRequest.getLastName())
            .andField(filterRequest.getFirstName())
            .andField(filterRequest.getIfscCode())
            .accept(filterRequest.getAccountNumber());
    }).isInstanceOf(BadRequestException.class)
            .hasMessage(NULL_ERROR_MESSAGE);
  }
}
