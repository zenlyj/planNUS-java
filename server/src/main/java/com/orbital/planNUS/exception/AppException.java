package com.orbital.planNUS.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
  private final String field;

  public AppException(String field, String message) {
    super(message);
    this.field = field;
  }
}
