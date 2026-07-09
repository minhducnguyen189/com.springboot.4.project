package com.springboot.project.transaction.repository;

import com.springboot.project.transaction.entity.TransactionDetailEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Component;

@Component
public class TransactionRepositoryAdapter implements ITransactionRepository {

    private final TransactionRepository transactionRepository;

    public TransactionRepositoryAdapter(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Optional<TransactionDetailEntity> findById(UUID id) {
        return transactionRepository.findById(id);
    }

    @Override
    public TransactionDetailEntity save(TransactionDetailEntity entity) {
        return transactionRepository.save(entity);
    }

    @Override
    public Page<TransactionDetailEntity> findAll(
            Specification<TransactionDetailEntity> spec, Pageable pageable) {
        return transactionRepository.findAll(spec, pageable);
    }

    @Override
    public List<TransactionDetailEntity> findBy(
            Specification<TransactionDetailEntity> spec,
            Function<
                            FluentQuery.FetchableFluentQuery<TransactionDetailEntity>,
                            List<TransactionDetailEntity>>
                    queryFunction) {
        return transactionRepository.findBy(spec, queryFunction);
    }

    @Override
    public long findMaxSequenceNumber() {
        return transactionRepository.findMaxSequenceNumber();
    }
}
