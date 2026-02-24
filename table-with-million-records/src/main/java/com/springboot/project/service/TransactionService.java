package com.springboot.project.service;

import com.springboot.project.entity.BankAccountEntity;
import com.springboot.project.entity.TransactionDetailEntity;
import com.springboot.project.exception.ResourceNotFoundException;
import com.springboot.project.generated.model.CreateTransactionRequestModel;
import com.springboot.project.generated.model.TransactionDetailModel;
import com.springboot.project.generated.model.TransactionFilterRequestModel;
import com.springboot.project.generated.model.TransactionFilterResponseModel;
import com.springboot.project.mapper.TransactionDetailMapper;
import com.springboot.project.repository.BankAccountRepository;
import com.springboot.project.repository.TransactionRepository;
import com.springboot.project.shared.SpecificationHelper;
import java.text.MessageFormat;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

        private static final String BANK_ACCOUNT_NOT_FOUND = "Bank account with id {0} not found";

        private final TransactionRepository transactionRepository;
        private final BankAccountRepository bankAccountRepository;

        @Autowired
        public TransactionService(
                        TransactionRepository transactionRepository,
                        BankAccountRepository bankAccountRepository) {
                this.transactionRepository = transactionRepository;
                this.bankAccountRepository = bankAccountRepository;
        }

        public TransactionDetailModel createTransaction(
                        CreateTransactionRequestModel createRequest) {
                BankAccountEntity bankAccount = bankAccountRepository
                                .findById(createRequest.getBankAccountId())
                                .orElseThrow(
                                                () -> new ResourceNotFoundException(
                                                                MessageFormat.format(
                                                                                BANK_ACCOUNT_NOT_FOUND,
                                                                                createRequest
                                                                                                .getBankAccountId())));

                TransactionDetailEntity entity = TransactionDetailMapper.MAPPER
                                .toTransactionDetailEntity(createRequest);
                entity.setBankAccount(bankAccount);

                TransactionDetailEntity savedEntity = transactionRepository.save(entity);
                return TransactionDetailMapper.MAPPER.toTransactionDetail(savedEntity);
        }

        public TransactionFilterResponseModel filterTransactions(
                        TransactionFilterRequestModel filterRequest) {
                assert filterRequest.getPagination() != null;
                Pageable pageable = SpecificationHelper.buildPageable(filterRequest.getPagination());
                Example<TransactionDetailEntity> transactionDetailEntityExample = this
                                .buildTransactionDetailExample(filterRequest);
                Specification<TransactionDetailEntity> specification = SpecificationHelper
                                .init(transactionDetailEntityExample);
                Page<TransactionDetailEntity> pages = this.transactionRepository.findAll(specification, pageable);

                List<TransactionDetailModel> data = TransactionDetailMapper.MAPPER.toTransactionDetails(pages.toList());
                return new TransactionFilterResponseModel()
                                .data(data)
                                .foundItems((long) pages.getNumberOfElements())
                                .totalItems(pages.getTotalElements());
        }

        public TransactionFilterResponseModel filterTransactionsWithCursor(
                        TransactionFilterRequestModel filterRequest) {
                assert filterRequest.getPagination() != null;
                Pageable pageable = SpecificationHelper.buildPageableForCursor(filterRequest.getPagination());

                Specification<TransactionDetailEntity> specification = SpecificationHelper
                                .init(this.buildTransactionDetailExample(filterRequest));

                var pagination = filterRequest.getPagination();
                Long nextPageToken = pagination != null ? pagination.getNextPageToken() : null;
                Long previousPageToken = pagination != null ? pagination.getPreviousPageToken() : null;

                if (nextPageToken != null) {
                        specification = specification.and(
                                        SpecificationHelper.cursorPagination(
                                                        pageable.getSort(), "sequenceNumber", nextPageToken, false));
                }

                if (previousPageToken != null) {
                        specification = specification.and(
                                        SpecificationHelper.cursorPagination(
                                                        pageable.getSort(),
                                                        "sequenceNumber",
                                                        previousPageToken,
                                                        true));
                        pageable = PageRequest.of(
                                        pageable.getPageNumber(),
                                        pageable.getPageSize(),
                                        pageable.getSort().isSorted()
                                                        ? pageable.getSort().descending()
                                                        : pageable.getSort());
                }

                Page<TransactionDetailEntity> pages = transactionRepository.findAll(specification, pageable);

                List<TransactionDetailModel> data = TransactionDetailMapper.MAPPER.toTransactionDetails(pages.toList());

                Long nextToken = null;
                Long previousToken = null;
                if (!data.isEmpty()) {
                        nextToken = data.get(data.size() - 1).getSequenceNumber();
                        previousToken = data.get(0).getSequenceNumber();
                }

                return new TransactionFilterResponseModel()
                                .data(data)
                                .foundItems((long) data.size())
                                .totalItems(pages.getTotalElements())
                                .previousPageToken(previousToken)
                                .nextPageToken(nextToken);
        }

        private Example<TransactionDetailEntity> buildTransactionDetailExample(
                        TransactionFilterRequestModel filterRequestModel) {
                TransactionDetailEntity transactionDetail = TransactionDetailMapper.MAPPER
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
