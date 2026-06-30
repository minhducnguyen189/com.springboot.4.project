package com.springboot.project.transaction.service;

import com.springboot.project.transaction.model.CreateTransactionRequestModel;
import com.springboot.project.transaction.model.TransactionDetailModel;
import com.springboot.project.transaction.model.TransactionFilterRequestModel;
import com.springboot.project.transaction.model.TransactionFilterResponseModel;
import com.springboot.project.transaction.model.UpdateTransactionRequestModel;
import java.util.UUID;

public interface ITransactionService {

    TransactionDetailModel getTransaction(UUID transactionId);

    TransactionDetailModel createTransaction(CreateTransactionRequestModel request);

    TransactionDetailModel updateTransaction(
            UUID transactionId, UpdateTransactionRequestModel request);

    void deleteTransaction(UUID transactionId);

    TransactionFilterResponseModel filterTransactions(
            TransactionFilterRequestModel request);

    TransactionFilterResponseModel filterTransactionsWithCursor(
            TransactionFilterRequestModel request);
}
