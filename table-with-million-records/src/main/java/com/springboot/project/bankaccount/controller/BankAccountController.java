package com.springboot.project.bankaccount.controller;

import com.springboot.project.bankaccount.generated.api.BankAccountApi;
import com.springboot.project.bankaccount.generated.model.BankAccountFilterRequestModel;
import com.springboot.project.bankaccount.generated.model.BankAccountFilterResponseModel;
import com.springboot.project.bankaccount.service.IBankAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BankAccountController implements BankAccountApi {

    private final IBankAccountService bankAccountService;

    public BankAccountController(IBankAccountService bankAccountService) {
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
