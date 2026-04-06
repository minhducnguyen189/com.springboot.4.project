package com.springboot.project.dao;

import com.springboot.project.dao.repository.BankAccountRepository;
import com.springboot.project.dao.repository.GenericRepository;
import com.springboot.project.dao.repository.LoginUserRepository;
import com.springboot.project.dao.repository.TransactionRepository;

public enum RepositoryTypeEnum {

  BANK_ACCOUNT(BankAccountRepository.class),
  TRANSACTION(TransactionRepository.class),
  LOGIN_USER(LoginUserRepository.class);

  private final Class<? extends GenericRepository<?>> repoClass;

  RepositoryTypeEnum(Class<? extends GenericRepository<?>> repoClass) {
    this.repoClass = repoClass;
  }

  public Class<? extends GenericRepository<?>> getRepoClass() {
    return repoClass;
  }

}
