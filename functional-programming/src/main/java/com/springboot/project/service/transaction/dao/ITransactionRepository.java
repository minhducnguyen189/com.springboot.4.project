package com.springboot.project.service.transaction.dao;

import com.springboot.project.dao.entity.TransactionDetailEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.FluentQuery;

public interface ITransactionRepository {

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
