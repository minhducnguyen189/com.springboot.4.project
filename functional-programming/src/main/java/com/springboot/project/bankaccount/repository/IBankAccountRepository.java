package com.springboot.project.bankaccount.repository;

import com.springboot.project.bankaccount.entity.BankAccountEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import com.springboot.project.common.repository.GenericRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.FluentQuery;

public interface IBankAccountRepository extends GenericRepository<BankAccountEntity> {

    Optional<BankAccountEntity> findById(UUID id);

    BankAccountEntity save(BankAccountEntity entity);

    Page<BankAccountEntity> findAll(
            Specification<BankAccountEntity> spec, Pageable pageable);

    List<BankAccountEntity> findBy(
            Specification<BankAccountEntity> spec,
            Function<
                            FluentQuery.FetchableFluentQuery<BankAccountEntity>,
                            List<BankAccountEntity>>
                    queryFunction);

    long findMaxSequenceNumber();
}
