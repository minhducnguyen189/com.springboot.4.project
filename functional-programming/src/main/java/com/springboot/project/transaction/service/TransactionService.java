package com.springboot.project.transaction.service;

import com.springboot.project.bankaccount.entity.BankAccountEntity;
import com.springboot.project.common.exception.ResourceNotFoundException;
import com.springboot.project.common.generated.dto.PaginationRequestDto;
import com.springboot.project.common.specification.SpecificationHelper;
import com.springboot.project.common.validation.Validations;
import com.springboot.project.transaction.entity.TransactionDetailEntity;
import com.springboot.project.transaction.entity.TransactionStatusEnumEntity;
import com.springboot.project.transaction.mapper.TransactionModelMapper;
import com.springboot.project.transaction.model.CreateTransactionRequestModel;
import com.springboot.project.transaction.model.TransactionDetailModel;
import com.springboot.project.transaction.model.TransactionFilterRequestModel;
import com.springboot.project.transaction.model.TransactionFilterResponseModel;
import com.springboot.project.transaction.model.UpdateTransactionRequestModel;
import com.springboot.project.transaction.repository.ITransactionBankAccountRepository;
import com.springboot.project.transaction.repository.ITransactionRepository;
import com.springboot.project.transaction.validation.TransactionFilterRequestValidation;
import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService implements ITransactionService {

    private static final String BANK_ACCOUNT_NOT_FOUND = "Bank account with id {0} not found";
    private static final String TRANSACTION_NOT_FOUND = "Transaction with id {0} not found";

    private final ITransactionRepository transactionRepository;
    private final ITransactionBankAccountRepository bankAccountRepository;

    public TransactionService(
            ITransactionRepository transactionRepository,
            ITransactionBankAccountRepository bankAccountRepository) {
        this.transactionRepository = transactionRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionDetailModel getTransaction(UUID transactionId) {
        Validations.itemMustNotBeNull()
                .accept(transactionId);
        TransactionDetailEntity entity = transactionRepository
                .findById(transactionId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                MessageFormat.format(
                                        TRANSACTION_NOT_FOUND, transactionId)));
        return TransactionModelMapper.MAPPER.toTransactionDetail(entity);
    }

    @Override
    @Transactional
    public TransactionDetailModel createTransaction(
            CreateTransactionRequestModel createRequest) {
        Validations.itemMustNotBeNull()
                .andField(createRequest.getBankAccountId())
                .andField(createRequest.getTaxAmount())
                .andField(createRequest.getNetValue())
                .andField(createRequest.getPaymentMethod())
                .andField(createRequest.getValue())
                .accept(createRequest.getDate());
        Validations.numberMustBePositive()
                .andField(createRequest.getTaxAmount())
                .andField(createRequest.getNetValue())
                .andField(createRequest.getValue());
        Validations.stringMustNotBeBlank()
                .andField(createRequest.getLocation());

        BankAccountEntity bankAccount = bankAccountRepository
                .findById(createRequest.getBankAccountId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                MessageFormat.format(
                                        BANK_ACCOUNT_NOT_FOUND,
                                        createRequest
                                                .getBankAccountId())));

        TransactionDetailEntity entity = TransactionModelMapper.MAPPER
                .toTransactionDetailEntity(createRequest);
        entity.setBankAccount(bankAccount);

        TransactionDetailEntity savedEntity = transactionRepository.save(entity);
        return TransactionModelMapper.MAPPER.toTransactionDetail(savedEntity);
    }

    @Override
    @Transactional
    public TransactionDetailModel updateTransaction(
            UUID transactionId, UpdateTransactionRequestModel updateRequest) {
        Validations.itemMustNotBeNull()
                .accept(transactionId);
        if (updateRequest.getValue() != null) {
            Validations.numberMustBePositive()
                    .accept(updateRequest.getValue());
        }
        if (updateRequest.getTaxAmount() != null) {
            Validations.numberMustBePositive()
                    .accept(updateRequest.getTaxAmount());
        }
        if (updateRequest.getNetValue() != null) {
            Validations.numberMustBePositive()
                    .accept(updateRequest.getNetValue());
        }
        if (updateRequest.getLocation() != null) {
            Validations.stringMustNotBeBlank()
                    .accept(updateRequest.getLocation());
        }
        TransactionDetailEntity existingEntity = transactionRepository
                .findById(transactionId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                MessageFormat.format(
                                        TRANSACTION_NOT_FOUND, transactionId)));

        if (updateRequest.getBankAccountId() != null) {
            BankAccountEntity bankAccount = bankAccountRepository
                    .findById(updateRequest.getBankAccountId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException(
                                    MessageFormat.format(
                                            BANK_ACCOUNT_NOT_FOUND,
                                            updateRequest.getBankAccountId())));
            existingEntity.setBankAccount(bankAccount);
        }

        TransactionModelMapper.MAPPER.updateTransactionDetailEntity(
                updateRequest, existingEntity);

        TransactionDetailEntity savedEntity = transactionRepository.save(existingEntity);
        return TransactionModelMapper.MAPPER.toTransactionDetail(savedEntity);
    }

    @Override
    @Transactional
    public void deleteTransaction(UUID transactionId) {
        Validations.itemMustNotBeNull()
                .accept(transactionId);
        TransactionDetailEntity existingEntity = transactionRepository
                .findById(transactionId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                MessageFormat.format(
                                        TRANSACTION_NOT_FOUND, transactionId)));

        existingEntity.setStatus(TransactionStatusEnumEntity.DELETED);
        transactionRepository.save(existingEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionFilterResponseModel filterTransactions(
            TransactionFilterRequestModel filterRequest) {
        return executeFilter(filterRequest, SpecificationHelper::buildPageable);
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionFilterResponseModel filterTransactionsWithCursor(
            TransactionFilterRequestModel filterRequest) {
        return executeFilter(filterRequest, pagination ->
                SpecificationHelper.buildPageableForCursor(pagination, "sequenceNumber"));
    }

    private TransactionFilterResponseModel executeFilter(
            TransactionFilterRequestModel filterRequest,
            Function<PaginationRequestDto, Pageable> pageableBuilder) {
        TransactionFilterRequestValidation.validate().accept(filterRequest);

        Pageable pageable = pageableBuilder.apply(filterRequest.getPagination());
        Example<TransactionDetailEntity> example = this.buildTransactionDetailExample(filterRequest);
        Specification<TransactionDetailEntity> specification = SpecificationHelper.init(example);
        Page<TransactionDetailEntity> pages = this.transactionRepository.findAll(specification, pageable);

        List<TransactionDetailModel> data = TransactionModelMapper.MAPPER
                .toTransactionDetails(pages.toList());
        return TransactionFilterResponseModel.builder()
                .data(data)
                .foundItems(Long.valueOf(pages.getNumberOfElements()))
                .totalItems(pages.getTotalElements())
                .build();
    }

    private Example<TransactionDetailEntity> buildTransactionDetailExample(
            TransactionFilterRequestModel filterRequestModel) {
        TransactionDetailEntity transactionDetail = TransactionModelMapper.MAPPER
                .toTransactionDetailEntityFromExample(
                        filterRequestModel);

        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withNullHandler(ExampleMatcher.NullHandler.IGNORE)
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        return Example.of(transactionDetail, exampleMatcher);
    }
}