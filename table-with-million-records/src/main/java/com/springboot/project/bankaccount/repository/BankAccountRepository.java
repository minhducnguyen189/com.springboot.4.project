package com.springboot.project.bankaccount.repository;

import com.springboot.project.bankaccount.entity.BankAccountEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository
                extends JpaRepository<BankAccountEntity, UUID>,
                JpaSpecificationExecutor<BankAccountEntity> {

        @Query("SELECT MAX(t.sequenceNumber) FROM BankAccountEntity t")
        long findMaxSequenceNumber();
}
