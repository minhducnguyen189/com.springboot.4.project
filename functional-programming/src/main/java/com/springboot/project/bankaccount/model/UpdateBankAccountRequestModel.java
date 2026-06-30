package com.springboot.project.bankaccount.model;

import com.springboot.project.bankaccount.generated.dto.AccountStatusEnumDto;
import com.springboot.project.bankaccount.generated.dto.AccountTypeEnumDto;
import com.springboot.project.bankaccount.generated.dto.CurrencyEnumDto;
import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateBankAccountRequestModel {

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    private String street;

    private String streetNumber;

    private String postalCode;

    private String city;

    private String country;

    private String accountNumber;

    private AccountTypeEnumDto accountType;

    private String ifscCode;

    private BigDecimal balance;

    private CurrencyEnumDto currency;

    private AccountStatusEnumDto status;
}