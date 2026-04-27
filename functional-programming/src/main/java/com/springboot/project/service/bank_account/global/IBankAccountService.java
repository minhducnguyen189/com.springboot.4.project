package com.springboot.project.service.bank_account.global;

import com.springboot.project.service.bank_account.model.BankAccountDetailModel;
import com.springboot.project.service.bank_account.model.BankAccountFilterRequestModel;
import com.springboot.project.service.bank_account.model.BankAccountFilterResponseModel;
import com.springboot.project.service.bank_account.model.CreateBankAccountRequestModel;
import com.springboot.project.service.bank_account.model.UpdateBankAccountRequestModel;
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
