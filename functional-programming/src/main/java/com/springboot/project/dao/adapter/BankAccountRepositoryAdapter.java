package com.springboot.project.dao.adapter;

import com.springboot.project.dao.entity.BankAccountEntity;
import com.springboot.project.dao.repository.BankAccountRepository;
import com.springboot.project.service.bank_account.dao.IBankAccountRepository;
import java.util.List;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Component;

@Component
public class BankAccountRepositoryAdapter implements IBankAccountRepository {

    private final BankAccountRepository bankAccountRepository;

    @Autowired
    public BankAccountRepositoryAdapter(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public Page<BankAccountEntity> findAll(
            Specification<BankAccountEntity> spec, Pageable pageable) {
        return bankAccountRepository.findAll(spec, pageable);
    }

    @Override
    public List<BankAccountEntity> findBy(
            Specification<BankAccountEntity> spec,
            Function<
                            FluentQuery.FetchableFluentQuery<BankAccountEntity>,
                            List<BankAccountEntity>>
                    queryFunction) {
        return bankAccountRepository.findBy(spec, queryFunction);
    }

    @Override
    public long findMaxSequenceNumber() {
        return bankAccountRepository.findMaxSequenceNumber();
    }
}
