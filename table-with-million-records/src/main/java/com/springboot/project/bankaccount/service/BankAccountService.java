package com.springboot.project.bankaccount.service;

import com.springboot.project.bankaccount.entity.BankAccountEntity;
import com.springboot.project.bankaccount.generated.model.BankAccountDetailModel;
import com.springboot.project.bankaccount.generated.model.BankAccountFilterRequestModel;
import com.springboot.project.bankaccount.generated.model.BankAccountFilterResponseModel;
import com.springboot.project.bankaccount.mapper.BankAccountMapper;
import com.springboot.project.bankaccount.repository.BankAccountRepository;
import com.springboot.project.common.exception.BadRequestException;
import com.springboot.project.common.specification.SpecificationHelper;
import com.springboot.project.common.specification.SpecificationHelper.CursorQuery;
import java.util.List;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BankAccountService implements IBankAccountService {

    private static final String SEQUENCE_NUMBER = "sequenceNumber";
    private static final String PAGINATION_REQUIRED = "Pagination must not be null";

    private final BankAccountRepository bankAccountRepository;

    public BankAccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public BankAccountFilterResponseModel filterBankAccounts(
            BankAccountFilterRequestModel filterRequest) {
        requirePagination(filterRequest);
        Pageable pageable = SpecificationHelper.buildPageable(filterRequest.getPagination());
        Specification<BankAccountEntity> specification =
                SpecificationHelper.init(buildBankAccountExample(filterRequest));
        Page<BankAccountEntity> page = bankAccountRepository.findAll(specification, pageable);

        List<BankAccountDetailModel> data =
                BankAccountMapper.MAPPER.toBankAccountDetails(page.toList());
        return new BankAccountFilterResponseModel()
                .data(data)
                .foundItems((long) page.getNumberOfElements())
                .totalItems(page.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public BankAccountFilterResponseModel filterBankAccountsWithCursor(
            BankAccountFilterRequestModel filterRequest) {
        requirePagination(filterRequest);
        CursorQuery<BankAccountEntity> cursorQuery =
                SpecificationHelper.buildCursorQuery(
                        SpecificationHelper.init(buildBankAccountExample(filterRequest)),
                        filterRequest.getPagination(),
                        SEQUENCE_NUMBER);
        List<BankAccountEntity> entities =
                bankAccountRepository.findBy(
                        cursorQuery.specification(),
                        SpecificationHelper.limitedSortedQuery(
                                cursorQuery.sort(), cursorQuery.pageSize()));

        List<BankAccountDetailModel> data = BankAccountMapper.MAPPER.toBankAccountDetails(entities);
        Long nextToken = data.isEmpty() ? null : data.get(data.size() - 1).getSequenceNumber();
        Long previousToken = data.isEmpty() ? null : data.get(0).getSequenceNumber();

        return new BankAccountFilterResponseModel()
                .data(data)
                .totalItems(bankAccountRepository.findMaxSequenceNumber())
                .foundItems((long) data.size())
                .previousPageToken(previousToken)
                .nextPageToken(nextToken);
    }

    private void requirePagination(BankAccountFilterRequestModel filterRequest) {
        if (filterRequest.getPagination() == null) {
            throw new BadRequestException(PAGINATION_REQUIRED);
        }
    }

    private Example<BankAccountEntity> buildBankAccountExample(
            BankAccountFilterRequestModel filterRequestModel) {
        BankAccountEntity bankAccount =
                BankAccountMapper.MAPPER.toBankAccountEntityFromExample(filterRequestModel);
        return Example.of(bankAccount, SpecificationHelper.containingIgnoreCaseMatcher());
    }
}
