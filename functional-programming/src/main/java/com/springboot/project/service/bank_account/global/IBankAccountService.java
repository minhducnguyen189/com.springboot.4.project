package com.springboot.project.service.bank_account.global;

import com.springboot.project.service.bank_account.model.BankAccountFilterRequestModel;
import com.springboot.project.service.bank_account.model.BankAccountFilterResponseModel;

public interface IBankAccountService {

    BankAccountFilterResponseModel filterBankAccounts(
            BankAccountFilterRequestModel bankAccountFilterRequestModel);

    BankAccountFilterResponseModel filterBankAccountsWithCursor(
            BankAccountFilterRequestModel bankAccountFilterRequestModel);
    
}
