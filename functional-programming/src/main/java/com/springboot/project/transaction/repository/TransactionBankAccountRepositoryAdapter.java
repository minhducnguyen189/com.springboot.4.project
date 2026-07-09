package com.springboot.project.transaction.repository;

import com.springboot.project.bankaccount.entity.BankAccountEntity;
import com.springboot.project.bankaccount.repository.BankAccountRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class TransactionBankAccountRepositoryAdapter
        implements ITransactionBankAccountRepository {

    private final BankAccountRepository bankAccountRepository;

    public TransactionBankAccountRepositoryAdapter(
            BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public Optional<BankAccountEntity> findById(UUID id) {
        return bankAccountRepository.findById(id);
    }
}
