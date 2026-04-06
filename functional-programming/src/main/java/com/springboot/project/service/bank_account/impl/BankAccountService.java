package com.springboot.project.service.bank_account.impl;

import com.springboot.project.dao.entity.BankAccountEntity;
import com.springboot.project.service.bank_account.dao.IBankAccountRepository;
import com.springboot.project.service.bank_account.global.IBankAccountService;
import com.springboot.project.service.bank_account.mapper.BankAccountModelMapper;
import com.springboot.project.service.bank_account.model.BankAccountDetailModel;
import com.springboot.project.service.bank_account.model.BankAccountFilterRequestModel;
import com.springboot.project.service.bank_account.model.BankAccountFilterResponseModel;
import com.springboot.project.shared.SpecificationHelper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
        assert filterRequest.getPagination() != null;
        Pageable pageable = SpecificationHelper.buildPageable(filterRequest.getPagination());
        Example<BankAccountEntity> bankAccountEntityExample =
                this.buildBankAccountExample(filterRequest);
        Specification<BankAccountEntity> specification =
                SpecificationHelper.init(bankAccountEntityExample);
        Page<BankAccountEntity> pages =
                this.bankAccountRepository.findAll(specification, pageable);

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

    @Override
    public BankAccountFilterResponseModel filterBankAccountsWithCursor(
            BankAccountFilterRequestModel filterRequest) {
        assert filterRequest.getPagination() != null;
        Pageable pageable =
                SpecificationHelper.buildPageableForCursor(
                        filterRequest.getPagination(), "sequenceNumber");
        Specification<BankAccountEntity> specification =
                SpecificationHelper.init(this.buildBankAccountExample(filterRequest));

        var pagination = filterRequest.getPagination();
        Long nextPageToken = pagination != null ? pagination.getNextPageToken() : null;
        Long previousPageToken =
                pagination != null ? pagination.getPreviousPageToken() : null;

        Sort sort = pageable.getSort();

        if (nextPageToken != null) {
            specification =
                    specification.and(
                            SpecificationHelper.cursorPagination(
                                    sort, "sequenceNumber", nextPageToken, false));
        }

        if (previousPageToken != null) {
            specification =
                    specification.and(
                            SpecificationHelper.cursorPagination(
                                    sort, "sequenceNumber", previousPageToken, true));
            if (sort.isSorted()) {
                sort = sort.descending();
            }
        }

        final Sort finalSort = sort;
        final int pageSize = pageable.getPageSize();
        List<BankAccountEntity> entities =
                this.bankAccountRepository.findBy(
                        specification,
                        q ->
                                finalSort.isSorted()
                                        ? q.sortBy(finalSort).limit(pageSize).all()
                                        : q.limit(pageSize).all());

        List<BankAccountDetailModel> data =
                BankAccountModelMapper.MAPPER.toBankAccountDetails(entities);

        Long nextToken = null;
        Long previousToken = null;
        if (!data.isEmpty()) {
            nextToken = data.get(data.size() - 1).getSequenceNumber();
            previousToken = data.get(0).getSequenceNumber();
        }

        return BankAccountFilterResponseModel.builder()
                .data(data)
                .totalItems(this.bankAccountRepository.findMaxSequenceNumber())
                .foundItems((long) data.size())
                .previousPageToken(previousToken)
                .nextPageToken(nextToken)
                .build();
    }
}
