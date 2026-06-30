package com.springboot.project.bankaccount.service;

import com.springboot.project.bankaccount.model.BankAccountDetailModel;
import com.springboot.project.bankaccount.model.BankAccountFilterRequestModel;
import com.springboot.project.bankaccount.model.BankAccountFilterResponseModel;
import com.springboot.project.bankaccount.model.CreateBankAccountRequestModel;
import com.springboot.project.bankaccount.model.UpdateBankAccountRequestModel;
import java.util.UUID;

public interface IBankAccountService {

    BankAccountDetailModel getBankAccount(UUID bankAccountId);

    BankAccountDetailModel createBankAccount(CreateBankAccountRequestModel request);

    BankAccountDetailModel updateBankAccount(
            UUID bankAccountId, UpdateBankAccountRequestModel request);

    void deleteBankAccount(UUID bankAccountId);

    BankAccountFilterResponseModel filterBankAccounts(
            BankAccountFilterRequestModel bankAccountFilterRequestModel);

    BankAccountFilterResponseModel filterBankAccountsWithCursor(
            BankAccountFilterRequestModel bankAccountFilterRequestModel);
}
