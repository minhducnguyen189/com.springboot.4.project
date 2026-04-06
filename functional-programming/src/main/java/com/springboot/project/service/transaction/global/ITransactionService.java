package com.springboot.project.service.transaction.global;

import com.springboot.project.service.transaction.model.CreateTransactionRequestModel;
import com.springboot.project.service.transaction.model.TransactionDetailModel;
import com.springboot.project.service.transaction.model.TransactionFilterRequestModel;
import com.springboot.project.service.transaction.model.TransactionFilterResponseModel;
import com.springboot.project.service.transaction.model.UpdateTransactionRequestModel;
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
