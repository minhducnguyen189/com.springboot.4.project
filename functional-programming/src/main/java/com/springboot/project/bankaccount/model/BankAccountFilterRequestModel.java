package com.springboot.project.bankaccount.model;

import com.springboot.project.bankaccount.generated.dto.AccountStatusEnumDto;
import com.springboot.project.bankaccount.generated.dto.AccountTypeEnumDto;
import com.springboot.project.common.generated.dto.PaginationRequestDto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankAccountFilterRequestModel {


  private String firstName;

  private String lastName;

  private String phone;

  private String email;

  private String accountNumber;

  private AccountTypeEnumDto accountType;

  private String ifscCode;

  private AccountStatusEnumDto status;

  private PaginationRequestDto pagination;

}
