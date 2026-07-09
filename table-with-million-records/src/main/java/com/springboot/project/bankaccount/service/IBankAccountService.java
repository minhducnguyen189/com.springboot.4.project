package com.springboot.project.bankaccount.service;

import com.springboot.project.bankaccount.generated.model.BankAccountFilterRequestModel;
import com.springboot.project.bankaccount.generated.model.BankAccountFilterResponseModel;

public interface IBankAccountService {

    BankAccountFilterResponseModel filterBankAccounts(
            BankAccountFilterRequestModel filterRequest);

    BankAccountFilterResponseModel filterBankAccountsWithCursor(
            BankAccountFilterRequestModel filterRequest);
}
