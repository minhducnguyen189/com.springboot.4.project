package com.springboot.project.dao.repository;

import com.springboot.project.dao.entity.TransactionDetailEntity;
import java.util.UUID;

import com.springboot.project.service.common.GenericRepository;
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
