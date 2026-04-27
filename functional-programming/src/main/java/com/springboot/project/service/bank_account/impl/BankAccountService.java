package com.springboot.project.service.bank_account.impl;

import com.springboot.project.dao.entity.BankAccountEntity;
import com.springboot.project.service.bank_account.dao.IBankAccountRepository;
import com.springboot.project.service.bank_account.global.IBankAccountService;
import com.springboot.project.service.bank_account.mapper.BankAccountModelMapper;
import com.springboot.project.service.bank_account.model.BankAccountDetailModel;
import com.springboot.project.service.bank_account.model.BankAccountFilterRequestModel;
import com.springboot.project.service.bank_account.model.BankAccountFilterResponseModel;
import com.springboot.project.service.bank_account.validation.BankAccountFilterRequestValidation;
import com.springboot.project.shared.SpecificationHelper;
import java.util.List;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.springboot.project.generated.dto.PaginationRequestDto;

@Service
public class BankAccountService implements IBankAccountService {

    private final IBankAccountRepository bankAccountRepository;

    @Autowired
    public BankAccountService(IBankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
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
        BankAccountFilterRequestValidation.validate().accept(filterRequest);

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