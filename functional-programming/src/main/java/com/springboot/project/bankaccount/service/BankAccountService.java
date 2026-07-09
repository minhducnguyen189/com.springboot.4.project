package com.springboot.project.bankaccount.service;

import com.springboot.project.bankaccount.entity.AccountStatusEnumEntity;
import com.springboot.project.bankaccount.entity.BankAccountEntity;
import com.springboot.project.bankaccount.mapper.BankAccountModelMapper;
import com.springboot.project.bankaccount.model.*;
import com.springboot.project.bankaccount.repository.IBankAccountRepository;
import com.springboot.project.common.exception.ResourceNotFoundException;
import com.springboot.project.common.generated.dto.PaginationRequestDto;
import com.springboot.project.common.specification.SpecificationHelper;
import com.springboot.project.common.validation.Validations;
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
public class BankAccountService implements IBankAccountService {

    private static final String BANK_ACCOUNT_NOT_FOUND = "Bank account with id {0} not found";

    private final IBankAccountRepository bankAccountRepository;

    public BankAccountService(IBankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public BankAccountDetailModel getBankAccount(UUID bankAccountId) {
        Validations.itemMustNotBeNull()
                .accept(bankAccountId);
        BankAccountEntity entity = bankAccountRepository
                .findById(bankAccountId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                MessageFormat.format(
                                        BANK_ACCOUNT_NOT_FOUND, bankAccountId)));
        return BankAccountModelMapper.MAPPER.toBankAccountDetail(entity);
    }

    @Override
    @Transactional
    public BankAccountDetailModel createBankAccount(
            CreateBankAccountRequestModel createRequest) {
        Validations.itemMustNotBeNull()
                .andField(createRequest.getFirstName())
                .andField(createRequest.getLastName())
                .andField(createRequest.getPhone())
                .andField(createRequest.getEmail())
                .andField(createRequest.getAccountNumber())
                .andField(createRequest.getAccountType())
                .andField(createRequest.getIfscCode())
                .andField(createRequest.getBalance())
                .andField(createRequest.getCurrency())
                .andField(createRequest.getStreet())
                .andField(createRequest.getStreetNumber())
                .andField(createRequest.getPostalCode())
                .andField(createRequest.getCity())
                .accept(createRequest.getCountry());

        Validations.stringMustNotBeBlank()
                .andField(createRequest.getFirstName())
                .andField(createRequest.getLastName())
                .andField(createRequest.getAccountNumber())
                .accept(createRequest.getPhone());

        Validations.stringMustMatchEmailPattern()
                .accept(createRequest.getEmail());

        Validations.stringMustMatchPhonePattern()
                .accept(createRequest.getPhone());

        Validations.stringMustMatchIfscPattern()
                .accept(createRequest.getIfscCode());

        Validations.numberMustBeNonNegative()
                .accept(createRequest.getBalance());

        BankAccountEntity entity = BankAccountModelMapper.MAPPER
                .toBankAccountEntity(createRequest);
        entity.setStatus(AccountStatusEnumEntity.ACTIVE);

        BankAccountEntity savedEntity = bankAccountRepository.save(entity);
        return BankAccountModelMapper.MAPPER.toBankAccountDetail(savedEntity);
    }

    @Override
    @Transactional
    public BankAccountDetailModel updateBankAccount(
            UUID bankAccountId, UpdateBankAccountRequestModel updateRequest) {
        Validations.itemMustNotBeNull()
                .accept(bankAccountId);
        if (updateRequest.getBalance() != null) {
            Validations.numberMustBeNonNegative()
                    .accept(updateRequest.getBalance());
        }
        if (updateRequest.getEmail() != null) {
            Validations.stringMustMatchEmailPattern()
                    .accept(updateRequest.getEmail());
        }
        if (updateRequest.getPhone() != null) {
            Validations.stringMustMatchPhonePattern()
                    .accept(updateRequest.getPhone());
        }
        if (updateRequest.getIfscCode() != null) {
            Validations.stringMustMatchIfscPattern()
                    .accept(updateRequest.getIfscCode());
        }
        BankAccountEntity existingEntity = bankAccountRepository
                .findById(bankAccountId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                MessageFormat.format(
                                        BANK_ACCOUNT_NOT_FOUND, bankAccountId)));

        BankAccountModelMapper.MAPPER.updateBankAccountEntity(
                updateRequest, existingEntity);

        BankAccountEntity savedEntity = bankAccountRepository.save(existingEntity);
        return BankAccountModelMapper.MAPPER.toBankAccountDetail(savedEntity);
    }

    @Override
    @Transactional
    public void deleteBankAccount(UUID bankAccountId) {
        Validations.itemMustNotBeNull()
                .accept(bankAccountId);
        BankAccountEntity existingEntity = bankAccountRepository
                .findById(bankAccountId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                MessageFormat.format(
                                        BANK_ACCOUNT_NOT_FOUND, bankAccountId)));

        existingEntity.setStatus(AccountStatusEnumEntity.CLOSED);
        bankAccountRepository.save(existingEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public BankAccountFilterResponseModel filterBankAccounts(
            BankAccountFilterRequestModel filterRequest) {
        return executeFilter(filterRequest, SpecificationHelper::buildPageable);
    }

    @Override
    @Transactional(readOnly = true)
    public BankAccountFilterResponseModel filterBankAccountsWithCursor(
            BankAccountFilterRequestModel filterRequest) {
        return executeFilter(filterRequest, pagination ->
                SpecificationHelper.buildPageableForCursor(pagination, "sequenceNumber"));
    }

    private BankAccountFilterResponseModel executeFilter(
            BankAccountFilterRequestModel filterRequest,
            Function<PaginationRequestDto, Pageable> pageableBuilder) {
        Pageable pageable = pageableBuilder.apply(filterRequest.getPagination());
        Example<BankAccountEntity> example = this.buildBankAccountExample(filterRequest);
        Specification<BankAccountEntity> specification = SpecificationHelper.init(example);
        Page<BankAccountEntity> pages = this.bankAccountRepository.findAll(specification, pageable);

        List<BankAccountDetailModel> data =
                BankAccountModelMapper.MAPPER.toBankAccountDetails(pages.toList());
        return BankAccountFilterResponseModel.builder()
                .data(data)
                .foundItems((long) pages.getNumberOfElements())
                .totalItems(pages.getTotalElements())
                .build();
    }

    private Example<BankAccountEntity> buildBankAccountExample(
            BankAccountFilterRequestModel filterRequestModel) {
        BankAccountEntity bankAccount =
                BankAccountModelMapper.MAPPER.toBankAccountEntityFromExample(filterRequestModel);

        ExampleMatcher exampleMatcher =
                ExampleMatcher.matching()
                        .withIgnoreNullValues()
                        .withIgnoreCase()
                        .withNullHandler(ExampleMatcher.NullHandler.IGNORE)
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        return Example.of(bankAccount, exampleMatcher);
    }
}