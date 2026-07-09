package com.springboot.project.transaction.controller;

import com.springboot.project.transaction.generated.api.TransactionApi;
import com.springboot.project.transaction.generated.model.CreateTransactionRequestModel;
import com.springboot.project.transaction.generated.model.TransactionDetailModel;
import com.springboot.project.transaction.generated.model.TransactionFilterRequestModel;
import com.springboot.project.transaction.generated.model.TransactionFilterResponseModel;
import com.springboot.project.transaction.generated.model.UpdateTransactionRequestModel;
import com.springboot.project.transaction.service.ITransactionService;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TransactionController implements TransactionApi {

    private final ITransactionService transactionService;

    public TransactionController(ITransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public ResponseEntity<TransactionDetailModel> getTransaction(UUID transactionId) {
        TransactionDetailModel response = transactionService.getTransaction(transactionId);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<TransactionDetailModel> createTransaction(
            CreateTransactionRequestModel createTransactionRequestModel) {
        TransactionDetailModel response = transactionService.createTransaction(createTransactionRequestModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<TransactionDetailModel> updateTransaction(
            UUID transactionId, UpdateTransactionRequestModel updateTransactionRequestModel) {
        TransactionDetailModel response = transactionService.updateTransaction(
                transactionId, updateTransactionRequestModel);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> deleteTransaction(UUID transactionId) {
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.noContent().build();
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
