package com.springboot.project.transaction.repository;

import com.springboot.project.bankaccount.entity.BankAccountEntity;
import com.springboot.project.common.repository.GenericRepository;

import java.util.Optional;
import java.util.UUID;

public interface ITransactionBankAccountRepository extends GenericRepository<BankAccountEntity> {

    Optional<BankAccountEntity> findById(UUID id);
}
