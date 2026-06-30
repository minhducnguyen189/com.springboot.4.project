package com.springboot.project.bankaccount.service;

import com.springboot.project.bankaccount.entity.AccountStatusEnumEntity;
import com.springboot.project.bankaccount.entity.BankAccountEntity;
import com.springboot.project.bankaccount.repository.IBankAccountRepository;
import com.springboot.project.bankaccount.service.IBankAccountService;
import com.springboot.project.bankaccount.mapper.BankAccountModelMapper;
import com.springboot.project.bankaccount.model.*;
import com.springboot.project.common.validation.Validations;
import com.springboot.project.common.specification.SpecificationHelper;
import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.springboot.project.common.exception.ResourceNotFoundException;
import com.springboot.project.common.generated.dto.PaginationRequestDto;

@Service
public class BankAccountService implements IBankAccountService {

    private static final String BANK_ACCOUNT_NOT_FOUND = "Bank account with id {0} not found";

    private final IBankAccountRepository bankAccountRepository;

    @Autowired
    public BankAccountService(IBankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
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
    public BankAccountDetailModel createBankAccount(
            CreateBankAccountRequestModel createRequest) {
        Validations.itemMustNotBeNull()
                .doTheSameWithField(createRequest.getFirstName())
                .doTheSameWithField(createRequest.getLastName())
                .doTheSameWithField(createRequest.getPhone())
                .doTheSameWithField(createRequest.getEmail())
                .doTheSameWithField(createRequest.getAccountNumber())
                .doTheSameWithField(createRequest.getAccountType())
                .doTheSameWithField(createRequest.getIfscCode())
                .doTheSameWithField(createRequest.getBalance())
                .doTheSameWithField(createRequest.getCurrency())
                .doTheSameWithField(createRequest.getStreet())
                .doTheSameWithField(createRequest.getStreetNumber())
                .doTheSameWithField(createRequest.getPostalCode())
                .doTheSameWithField(createRequest.getCity())
                .accept(createRequest.getCountry());

        Validations.stringMustNotBeBlank()
                .doTheSameWithField(createRequest.getFirstName())
                .doTheSameWithField(createRequest.getLastName())
                .doTheSameWithField(createRequest.getAccountNumber())
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
    public BankAccountFilterResponseModel filterBankAccounts(
            BankAccountFilterRequestModel filterRequest) {
        return executeFilter(filterRequest, SpecificationHelper::buildPageable);
    }

    @Override
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