package com.springboot.project.bankaccount.controller;

import com.springboot.project.bankaccount.mapper.BankAccountDtoMapper;
import com.springboot.project.bankaccount.generated.api.BankAccountApi;
import com.springboot.project.bankaccount.generated.dto.*;
import com.springboot.project.bankaccount.service.IBankAccountService;
import com.springboot.project.bankaccount.service.BankAccountService;
import com.springboot.project.bankaccount.model.*;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BankAccountController implements BankAccountApi {

    private final IBankAccountService bankAccountService;

    @Autowired
    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @Override
    public ResponseEntity<BankAccountDetailDto> getBankAccount(UUID bankAccountId) {
        BankAccountDetailModel responseModel = bankAccountService.getBankAccount(bankAccountId);
        return ResponseEntity.ok(BankAccountDtoMapper.MAPPER.toBankAccountDetailDto(responseModel));
    }

    @Override
    public ResponseEntity<BankAccountDetailDto> createBankAccount(
            CreateBankAccountRequestDto createBankAccountRequestDto) {
        CreateBankAccountRequestModel requestModel =
                BankAccountDtoMapper.MAPPER.toCreateBankAccountRequestModel(
                        createBankAccountRequestDto);
        BankAccountDetailModel responseModel = bankAccountService.createBankAccount(requestModel);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BankAccountDtoMapper.MAPPER.toBankAccountDetailDto(responseModel));
    }

    @Override
    public ResponseEntity<BankAccountDetailDto> updateBankAccount(
            UUID bankAccountId, UpdateBankAccountRequestDto updateBankAccountRequestDto) {
        UpdateBankAccountRequestModel requestModel =
                BankAccountDtoMapper.MAPPER.toUpdateBankAccountRequestModel(
                        updateBankAccountRequestDto);
        BankAccountDetailModel responseModel =
                bankAccountService.updateBankAccount(bankAccountId, requestModel);
        return ResponseEntity.ok(BankAccountDtoMapper.MAPPER.toBankAccountDetailDto(responseModel));
    }

    @Override
    public ResponseEntity<Void> deleteBankAccount(UUID bankAccountId) {
        bankAccountService.deleteBankAccount(bankAccountId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<BankAccountFilterResponseDto> filterBankAccounts(
            BankAccountFilterRequestDto bankAccountFilterRequestDto) {
        BankAccountFilterRequestModel bankAccountFilterRequestModel =
                BankAccountDtoMapper.MAPPER.toBankAccountFilterRequestModel(bankAccountFilterRequestDto);
        BankAccountFilterResponseModel responseModel = bankAccountService.filterBankAccounts(bankAccountFilterRequestModel);
        return ResponseEntity.ok(BankAccountDtoMapper.MAPPER.toBankAccountFilterResponseDto(responseModel));
    }

    @Override
    public ResponseEntity<BankAccountFilterResponseDto> filterBankAccountsWithCursor(
            BankAccountFilterRequestDto bankAccountFilterRequestDto) {
        BankAccountFilterRequestModel bankAccountFilterRequestModel =
                BankAccountDtoMapper.MAPPER.toBankAccountFilterRequestModel(bankAccountFilterRequestDto);
        BankAccountFilterResponseModel responseModel = bankAccountService.filterBankAccountsWithCursor(bankAccountFilterRequestModel);
        return ResponseEntity.ok(BankAccountDtoMapper.MAPPER.toBankAccountFilterResponseDto(responseModel));
    }
}
