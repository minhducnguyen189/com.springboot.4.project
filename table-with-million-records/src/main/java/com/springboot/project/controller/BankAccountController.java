package com.springboot.project.controller;

import com.springboot.project.generated.api.BankAccountApi;
import com.springboot.project.generated.model.BankAccountFilterRequestModel;
import com.springboot.project.generated.model.BankAccountFilterResponseModel;
import com.springboot.project.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BankAccountController implements BankAccountApi {

    private final BankAccountService bankAccountService;

    @Autowired
    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @Override
    public ResponseEntity<BankAccountFilterResponseModel> filterBankAccounts(
            BankAccountFilterRequestModel bankAccountFilterRequestModel) {
        BankAccountFilterResponseModel response = bankAccountService.filterBankAccounts(bankAccountFilterRequestModel);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<BankAccountFilterResponseModel> filterBankAccountsWithCursor(
            BankAccountFilterRequestModel bankAccountFilterRequestModel) {
        BankAccountFilterResponseModel response = bankAccountService
                .filterBankAccountsWithCursor(bankAccountFilterRequestModel);
        return ResponseEntity.ok(response);
    }
}
