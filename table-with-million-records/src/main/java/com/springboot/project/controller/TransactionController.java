package com.springboot.project.controller;

import com.springboot.project.generated.api.TransactionApi;
import com.springboot.project.generated.model.CreateTransactionRequestModel;
import com.springboot.project.generated.model.TransactionDetailModel;
import com.springboot.project.generated.model.TransactionFilterRequestModel;
import com.springboot.project.generated.model.TransactionFilterResponseModel;
import com.springboot.project.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController implements TransactionApi {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public ResponseEntity<TransactionDetailModel> createTransaction(
            CreateTransactionRequestModel createTransactionRequestModel) {
        TransactionDetailModel response = transactionService.createTransaction(createTransactionRequestModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<TransactionFilterResponseModel> filterTransactions(
            TransactionFilterRequestModel transactionFilterRequestModel) {
        TransactionFilterResponseModel response = transactionService.filterTransactions(transactionFilterRequestModel);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<TransactionFilterResponseModel> filterTransactionsWithCursor(
            TransactionFilterRequestModel transactionFilterRequestModel) {
        TransactionFilterResponseModel response = transactionService
                .filterTransactionsWithCursor(transactionFilterRequestModel);
        return ResponseEntity.ok(response);
    }
}
