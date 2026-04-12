package com.springboot.project.service.common;

import com.springboot.project.exception.UnsupportedRepositoryException;
import com.springboot.project.service.bank_account.dao.IBankAccountRepository;
import com.springboot.project.service.transaction.dao.ITransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class RepositoryFactory {

  private static final String UNSUPPORTED_REPOSITORY_ERROR_MESSAGE =
          "Unsupported for the repository type: {0}";

  private final IBankAccountRepository bankAccountRepository;
  private final ITransactionRepository transactionRepository;

  @Autowired
  public RepositoryFactory(IBankAccountRepository bankAccountRepository,
                           ITransactionRepository transactionRepository) {
    this.bankAccountRepository = bankAccountRepository;
    this.transactionRepository = transactionRepository;
  }


  public  <T extends GenericRepository<?>> T getRepository(
          RepositoryTypeEnum type, Class<T> expectedType) {
    GenericRepository<?> repo =
        switch (type) {
          case BANK_ACCOUNT -> this.bankAccountRepository;
          case TRANSACTION -> this.transactionRepository;
          default ->
              throw new UnsupportedRepositoryException(
                  MessageFormat.format(UNSUPPORTED_REPOSITORY_ERROR_MESSAGE, type.name()));
        };
    return expectedType.cast(repo);
  }


}
