package com.springboot.project.transaction.service;

import com.springboot.project.transaction.generated.model.CreateTransactionRequestModel;
import com.springboot.project.transaction.generated.model.TransactionDetailModel;
import com.springboot.project.transaction.generated.model.TransactionFilterRequestModel;
import com.springboot.project.transaction.generated.model.TransactionFilterResponseModel;
import com.springboot.project.transaction.generated.model.UpdateTransactionRequestModel;
import java.util.UUID;

public interface ITransactionService {

    TransactionDetailModel getTransaction(UUID transactionId);

    TransactionDetailModel createTransaction(CreateTransactionRequestModel createRequest);

    TransactionDetailModel updateTransaction(
            UUID transactionId, UpdateTransactionRequestModel updateRequest);

    void deleteTransaction(UUID transactionId);

    TransactionFilterResponseModel filterTransactions(TransactionFilterRequestModel filterRequest);

    TransactionFilterResponseModel filterTransactionsWithCursor(
            TransactionFilterRequestModel filterRequest);
}
