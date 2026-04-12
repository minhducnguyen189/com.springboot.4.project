package com.springboot.project.service.transaction.dao;

import com.springboot.project.dao.entity.BankAccountEntity;
import com.springboot.project.service.common.GenericRepository;

import java.util.Optional;
import java.util.UUID;

public interface ITransactionBankAccountRepository extends GenericRepository<BankAccountEntity> {

    Optional<BankAccountEntity> findById(UUID id);
}
