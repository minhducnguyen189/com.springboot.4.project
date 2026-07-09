package com.springboot.project.transaction.service;

import com.springboot.project.bankaccount.entity.BankAccountEntity;
import com.springboot.project.bankaccount.repository.BankAccountRepository;
import com.springboot.project.common.exception.BadRequestException;
import com.springboot.project.common.exception.ResourceNotFoundException;
import com.springboot.project.common.specification.SpecificationHelper;
import com.springboot.project.common.specification.SpecificationHelper.CursorQuery;
import com.springboot.project.transaction.entity.TransactionDetailEntity;
import com.springboot.project.transaction.entity.TransactionStatusEnumEntity;
import com.springboot.project.transaction.generated.model.CreateTransactionRequestModel;
import com.springboot.project.transaction.generated.model.TransactionDetailModel;
import com.springboot.project.transaction.generated.model.TransactionFilterRequestModel;
import com.springboot.project.transaction.generated.model.TransactionFilterResponseModel;
import com.springboot.project.transaction.generated.model.UpdateTransactionRequestModel;
import com.springboot.project.transaction.mapper.TransactionDetailMapper;
import com.springboot.project.transaction.repository.TransactionRepository;
import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService implements ITransactionService {

    private static final String SEQUENCE_NUMBER = "sequenceNumber";
    private static final String PAGINATION_REQUIRED = "Pagination must not be null";
    private static final String BANK_ACCOUNT_NOT_FOUND = "Bank account with id {0} not found";
    private static final String TRANSACTION_NOT_FOUND = "Transaction with id {0} not found";

    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;

    public TransactionService(
            TransactionRepository transactionRepository,
            BankAccountRepository bankAccountRepository) {
        this.transactionRepository = transactionRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionDetailModel getTransaction(UUID transactionId) {
        TransactionDetailEntity entity = findTransactionOrThrow(transactionId);
        return TransactionDetailMapper.MAPPER.toTransactionDetail(entity);
    }

    @Override
    @Transactional
    public TransactionDetailModel createTransaction(CreateTransactionRequestModel createRequest) {
        BankAccountEntity bankAccount = findBankAccountOrThrow(createRequest.getBankAccountId());

        TransactionDetailEntity entity =
                TransactionDetailMapper.MAPPER.toTransactionDetailEntity(createRequest);
        entity.setBankAccount(bankAccount);

        TransactionDetailEntity savedEntity = transactionRepository.save(entity);
        return TransactionDetailMapper.MAPPER.toTransactionDetail(savedEntity);
    }

    @Override
    @Transactional
    public TransactionDetailModel updateTransaction(
            UUID transactionId, UpdateTransactionRequestModel updateRequest) {
        TransactionDetailEntity existingEntity = findTransactionOrThrow(transactionId);

        if (updateRequest.getBankAccountId() != null) {
            BankAccountEntity bankAccount = findBankAccountOrThrow(updateRequest.getBankAccountId());
            existingEntity.setBankAccount(bankAccount);
        }

        TransactionDetailMapper.MAPPER.updateTransactionDetailEntity(updateRequest, existingEntity);

        TransactionDetailEntity savedEntity = transactionRepository.save(existingEntity);
        return TransactionDetailMapper.MAPPER.toTransactionDetail(savedEntity);
    }

    @Override
    @Transactional
    public void deleteTransaction(UUID transactionId) {
        TransactionDetailEntity existingEntity = findTransactionOrThrow(transactionId);
        existingEntity.setStatus(TransactionStatusEnumEntity.DELETED);
        transactionRepository.save(existingEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionFilterResponseModel filterTransactions(
            TransactionFilterRequestModel filterRequest) {
        requirePagination(filterRequest);
        Pageable pageable = SpecificationHelper.buildPageable(filterRequest.getPagination());
        Specification<TransactionDetailEntity> specification =
                SpecificationHelper.init(buildTransactionDetailExample(filterRequest));
        Page<TransactionDetailEntity> page = transactionRepository.findAll(specification, pageable);

        List<TransactionDetailModel> data =
                TransactionDetailMapper.MAPPER.toTransactionDetails(page.toList());
        return new TransactionFilterResponseModel()
                .data(data)
                .foundItems((long) page.getNumberOfElements())
                .totalItems(page.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionFilterResponseModel filterTransactionsWithCursor(
            TransactionFilterRequestModel filterRequest) {
        requirePagination(filterRequest);
        CursorQuery<TransactionDetailEntity> cursorQuery =
                SpecificationHelper.buildCursorQuery(
                        SpecificationHelper.init(buildTransactionDetailExample(filterRequest)),
                        filterRequest.getPagination(),
                        SEQUENCE_NUMBER);
        List<TransactionDetailEntity> entities =
                transactionRepository.findBy(
                        cursorQuery.specification(),
                        SpecificationHelper.limitedSortedQuery(
                                cursorQuery.sort(), cursorQuery.pageSize()));

        List<TransactionDetailModel> data =
                TransactionDetailMapper.MAPPER.toTransactionDetails(entities);
        Long nextToken = data.isEmpty() ? null : data.get(data.size() - 1).getSequenceNumber();
        Long previousToken = data.isEmpty() ? null : data.get(0).getSequenceNumber();

        return new TransactionFilterResponseModel()
                .data(data)
                .totalItems(transactionRepository.findMaxSequenceNumber())
                .foundItems((long) data.size())
                .previousPageToken(previousToken)
                .nextPageToken(nextToken);
    }

    private void requirePagination(TransactionFilterRequestModel filterRequest) {
        if (filterRequest.getPagination() == null) {
            throw new BadRequestException(PAGINATION_REQUIRED);
        }
    }

    private TransactionDetailEntity findTransactionOrThrow(UUID transactionId) {
        return transactionRepository
                .findById(transactionId)
                .orElseThrow(
                        () ->
                                new ResourceNotFoundException(
                                        MessageFormat.format(TRANSACTION_NOT_FOUND, transactionId)));
    }

    private BankAccountEntity findBankAccountOrThrow(UUID bankAccountId) {
        return bankAccountRepository
                .findById(bankAccountId)
                .orElseThrow(
                        () ->
                                new ResourceNotFoundException(
                                        MessageFormat.format(BANK_ACCOUNT_NOT_FOUND, bankAccountId)));
    }

    private Example<TransactionDetailEntity> buildTransactionDetailExample(
            TransactionFilterRequestModel filterRequestModel) {
        TransactionDetailEntity transactionDetail =
                TransactionDetailMapper.MAPPER.toTransactionDetailEntityFromExample(filterRequestModel);
        return Example.of(transactionDetail, SpecificationHelper.containingIgnoreCaseMatcher());
    }
}
