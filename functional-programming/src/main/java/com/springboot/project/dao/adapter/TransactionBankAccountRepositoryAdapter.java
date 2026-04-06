package com.springboot.project.dao.adapter;

import com.springboot.project.dao.entity.BankAccountEntity;
import com.springboot.project.dao.repository.BankAccountRepository;
import com.springboot.project.service.transaction.dao.ITransactionBankAccountRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionBankAccountRepositoryAdapter
        implements ITransactionBankAccountRepository {

    private final BankAccountRepository bankAccountRepository;

    @Autowired
    public TransactionBankAccountRepositoryAdapter(
            BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public Optional<BankAccountEntity> findById(UUID id) {
        return bankAccountRepository.findById(id);
    }
}
