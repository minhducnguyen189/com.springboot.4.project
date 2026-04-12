package com.springboot.project.service.common;

import com.springboot.project.service.bank_account.dao.IBankAccountRepository;
import com.springboot.project.service.transaction.dao.ITransactionRepository;

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
