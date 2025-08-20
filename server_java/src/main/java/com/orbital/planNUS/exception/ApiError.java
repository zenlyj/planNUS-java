package com.orbital.planNUS.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ApiError {
  private LocalDateTime timestamp;
  private int status;
  private List<FieldErrorResponse> errors;

  public ApiError(HttpStatus status, List<FieldErrorResponse> errors) {
    this.timestamp = LocalDateTime.now();
    this.status = status.value();
    this.errors = errors;
  }
}
