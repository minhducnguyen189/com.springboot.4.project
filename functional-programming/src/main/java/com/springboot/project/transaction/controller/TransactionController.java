package com.springboot.project.transaction.controller;

import com.springboot.project.transaction.mapper.TransactionDtoMapper;
import com.springboot.project.transaction.generated.api.TransactionApi;
import com.springboot.project.transaction.generated.dto.CreateTransactionRequestDto;
import com.springboot.project.transaction.generated.dto.TransactionDetailDto;
import com.springboot.project.transaction.generated.dto.TransactionFilterRequestDto;
import com.springboot.project.transaction.generated.dto.TransactionFilterResponseDto;
import com.springboot.project.transaction.generated.dto.UpdateTransactionRequestDto;
import com.springboot.project.transaction.service.ITransactionService;
import com.springboot.project.transaction.service.TransactionService;
import com.springboot.project.transaction.model.CreateTransactionRequestModel;
import com.springboot.project.transaction.model.TransactionDetailModel;
import com.springboot.project.transaction.model.TransactionFilterRequestModel;
import com.springboot.project.transaction.model.TransactionFilterResponseModel;
import com.springboot.project.transaction.model.UpdateTransactionRequestModel;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TransactionController implements TransactionApi {

    private final ITransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public ResponseEntity<TransactionDetailDto> getTransaction(UUID transactionId) {
        TransactionDetailModel responseModel = transactionService.getTransaction(transactionId);
        return ResponseEntity.ok(TransactionDtoMapper.MAPPER.toTransactionDetailDto(responseModel));
    }

    @Override
    public ResponseEntity<TransactionDetailDto> createTransaction(
            CreateTransactionRequestDto createTransactionRequestDto) {
        CreateTransactionRequestModel requestModel =
                TransactionDtoMapper.MAPPER.toCreateTransactionRequestModel(
                        createTransactionRequestDto);
        TransactionDetailModel responseModel = transactionService.createTransaction(requestModel);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TransactionDtoMapper.MAPPER.toTransactionDetailDto(responseModel));
    }

    @Override
    public ResponseEntity<TransactionDetailDto> updateTransaction(
            UUID transactionId, UpdateTransactionRequestDto updateTransactionRequestDto) {
        UpdateTransactionRequestModel requestModel =
                TransactionDtoMapper.MAPPER.toUpdateTransactionRequestModel(
                        updateTransactionRequestDto);
        TransactionDetailModel responseModel =
                transactionService.updateTransaction(transactionId, requestModel);
        return ResponseEntity.ok(TransactionDtoMapper.MAPPER.toTransactionDetailDto(responseModel));
    }

    @Override
    public ResponseEntity<Void> deleteTransaction(UUID transactionId) {
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<TransactionFilterResponseDto> filterTransactions(
            TransactionFilterRequestDto transactionFilterRequestDto) {
        TransactionFilterRequestModel requestModel =
                TransactionDtoMapper.MAPPER.toTransactionFilterRequestModel(
                        transactionFilterRequestDto);
        TransactionFilterResponseModel responseModel =
                transactionService.filterTransactions(requestModel);
        return ResponseEntity.ok(
                TransactionDtoMapper.MAPPER.toTransactionFilterResponseDto(responseModel));
    }

    @Override
    public ResponseEntity<TransactionFilterResponseDto> filterTransactionsWithCursor(
            TransactionFilterRequestDto transactionFilterRequestDto) {
        TransactionFilterRequestModel requestModel =
                TransactionDtoMapper.MAPPER.toTransactionFilterRequestModel(
                        transactionFilterRequestDto);
        TransactionFilterResponseModel responseModel =
                transactionService.filterTransactionsWithCursor(requestModel);
        return ResponseEntity.ok(
                TransactionDtoMapper.MAPPER.toTransactionFilterResponseDto(responseModel));
    }
}
