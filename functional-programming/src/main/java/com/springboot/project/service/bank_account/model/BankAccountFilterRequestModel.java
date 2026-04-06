package com.springboot.project.service.bank_account.model;

import com.springboot.project.generated.dto.AccountStatusEnumDto;
import com.springboot.project.generated.dto.AccountTypeEnumDto;
import com.springboot.project.generated.dto.PaginationRequestDto;
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
