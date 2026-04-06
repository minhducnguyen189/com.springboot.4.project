package com.springboot.project.service.transaction.dao;

import com.springboot.project.dao.entity.BankAccountEntity;
import java.util.Optional;
import java.util.UUID;

public interface ITransactionBankAccountRepository {

    Optional<BankAccountEntity> findById(UUID id);
}
