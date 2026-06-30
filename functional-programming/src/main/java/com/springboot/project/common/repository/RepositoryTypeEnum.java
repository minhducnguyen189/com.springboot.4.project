package com.springboot.project.common.repository;

import com.springboot.project.bankaccount.repository.IBankAccountRepository;
import com.springboot.project.transaction.repository.ITransactionRepository;

public enum RepositoryTypeEnum {

  BANK_ACCOUNT(IBankAccountRepository.class),
  TRANSACTION(ITransactionRepository.class);

  private final Class<? extends GenericRepository<?>> repoClass;

  RepositoryTypeEnum(Class<? extends GenericRepository<?>> repoClass) {
    this.repoClass = repoClass;
  }

  public Class<? extends GenericRepository<?>> getRepoClass() {
    return repoClass;
  }

}
