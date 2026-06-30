package com.springboot.project.bankaccount.repository;

import com.springboot.project.bankaccount.entity.BankAccountEntity;
import com.springboot.project.bankaccount.repository.BankAccountRepository;
import com.springboot.project.bankaccount.repository.IBankAccountRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
    public Optional<BankAccountEntity> findById(UUID id) {
        return bankAccountRepository.findById(id);
    }

    @Override
    public BankAccountEntity save(BankAccountEntity entity) {
        return bankAccountRepository.save(entity);
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
