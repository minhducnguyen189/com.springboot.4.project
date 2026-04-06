package com.springboot.project.dao.repository;

import com.springboot.project.dao.entity.BankAccountEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository
                extends JpaRepository<BankAccountEntity, UUID>,
                GenericRepository<BankAccountEntity>,
                JpaSpecificationExecutor<BankAccountEntity> {

        @Query("SELECT MAX(t.sequenceNumber) FROM BankAccountEntity t")
        long findMaxSequenceNumber();
}
