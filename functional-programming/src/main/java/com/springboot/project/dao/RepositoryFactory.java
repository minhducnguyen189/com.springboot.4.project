package com.springboot.project.dao;

import com.springboot.project.dao.repository.BankAccountRepository;
import com.springboot.project.dao.repository.GenericRepository;
import com.springboot.project.dao.repository.LoginUserRepository;
import com.springboot.project.dao.repository.TransactionRepository;
import com.springboot.project.exception.UnsupportedRepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class RepositoryFactory {

  private static final String UNSUPPORTED_REPOSITORY_ERROR_MESSAGE =
          "Unsupported for the repository type: {0}";

  private final BankAccountRepository bankAccountRepository;
  private final TransactionRepository transactionRepository;
  private final LoginUserRepository loginUserRepository;

  @Autowired
  public RepositoryFactory(BankAccountRepository bankAccountRepository,
                           TransactionRepository transactionRepository,
                           LoginUserRepository loginUserRepository) {
    this.bankAccountRepository = bankAccountRepository;
    this.transactionRepository = transactionRepository;
    this.loginUserRepository = loginUserRepository;
  }

  public  <T extends GenericRepository<?>> T getRepository(
          RepositoryTypeEnum type, Class<T> expectedType) {
    GenericRepository<?> repo =
        switch (type) {
          case BANK_ACCOUNT -> this.bankAccountRepository;
          case LOGIN_USER -> this.loginUserRepository;
          case TRANSACTION -> this.transactionRepository;
          default ->
              throw new UnsupportedRepositoryException(
                  MessageFormat.format(UNSUPPORTED_REPOSITORY_ERROR_MESSAGE, type.name()));
        };
    return expectedType.cast(repo);
  }


}
