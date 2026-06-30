package com.springboot.project.transaction.repository;

import com.springboot.project.transaction.entity.TransactionDetailEntity;
import java.util.UUID;

import com.springboot.project.common.repository.GenericRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository
                extends JpaRepository<TransactionDetailEntity, UUID>,
        GenericRepository<TransactionDetailEntity>,
                JpaSpecificationExecutor<TransactionDetailEntity> {

        @Query("SELECT MAX(t.sequenceNumber) FROM TransactionDetailEntity t")
        long findMaxSequenceNumber();

}
