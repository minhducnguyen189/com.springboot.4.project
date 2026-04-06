package com.springboot.project.exception;

public class UnsupportedRepositoryException extends RuntimeException {

  public UnsupportedRepositoryException(String message) {
    super(message);
  }
}
