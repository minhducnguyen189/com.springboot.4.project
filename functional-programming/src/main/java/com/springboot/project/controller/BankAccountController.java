package com.springboot.project.controller;

import com.springboot.project.controller.mapper.BankAccountDtoMapper;
import com.springboot.project.generated.api.BankAccountApi;
import com.springboot.project.generated.dto.BankAccountFilterRequestDto;
import com.springboot.project.generated.dto.BankAccountFilterResponseDto;
import com.springboot.project.service.bank_account.global.IBankAccountService;
import com.springboot.project.service.bank_account.impl.BankAccountService;
import com.springboot.project.service.bank_account.model.BankAccountFilterRequestModel;
import com.springboot.project.service.bank_account.model.BankAccountFilterResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BankAccountController implements BankAccountApi {

    private final IBankAccountService IBankAccountService;

    @Autowired
    public BankAccountController(BankAccountService bankAccountService) {
        this.IBankAccountService = bankAccountService;
    }

    @Override
    public ResponseEntity<BankAccountFilterResponseDto> filterBankAccounts(
            BankAccountFilterRequestDto bankAccountFilterRequestDto) {
        BankAccountFilterRequestModel bankAccountFilterRequestModel =
                BankAccountDtoMapper.MAPPER.toBankAccountFilterRequestModel(bankAccountFilterRequestDto);
        BankAccountFilterResponseModel responseModel = IBankAccountService.filterBankAccounts(bankAccountFilterRequestModel);
        return ResponseEntity.ok(BankAccountDtoMapper.MAPPER.toBankAccountFilterResponseDto(responseModel));
    }

    @Override
    public ResponseEntity<BankAccountFilterResponseDto> filterBankAccountsWithCursor(
            BankAccountFilterRequestDto bankAccountFilterRequestDto) {
        BankAccountFilterRequestModel bankAccountFilterRequestModel =
                BankAccountDtoMapper.MAPPER.toBankAccountFilterRequestModel(bankAccountFilterRequestDto);
        BankAccountFilterResponseModel responseModel = IBankAccountService.filterBankAccountsWithCursor(bankAccountFilterRequestModel);
        return ResponseEntity.ok(BankAccountDtoMapper.MAPPER.toBankAccountFilterResponseDto(responseModel));
    }
}
