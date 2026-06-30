package com.springboot.project.transaction.repository;

import com.springboot.project.transaction.entity.TransactionDetailEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import com.springboot.project.common.repository.GenericRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.FluentQuery;

public interface ITransactionRepository extends GenericRepository<TransactionDetailEntity> {

    Optional<TransactionDetailEntity> findById(UUID id);

    TransactionDetailEntity save(TransactionDetailEntity entity);

    Page<TransactionDetailEntity> findAll(
            Specification<TransactionDetailEntity> spec, Pageable pageable);

    List<TransactionDetailEntity> findBy(
            Specification<TransactionDetailEntity> spec,
            Function<
                            FluentQuery.FetchableFluentQuery<TransactionDetailEntity>,
                            List<TransactionDetailEntity>>
                    queryFunction);

    long findMaxSequenceNumber();
}
