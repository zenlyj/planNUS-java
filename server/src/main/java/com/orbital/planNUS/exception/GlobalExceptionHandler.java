package com.orbital.planNUS.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

  // @Valid on @RequestBody
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex) {
    List<FieldErrorResponse> errors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(error -> new FieldErrorResponse(error.getField(), error.getDefaultMessage()))
            .toList();

    return ResponseEntity.badRequest().body(new ApiError(HttpStatus.BAD_REQUEST, errors));
  }

  // @Validated on @RequestParam, @PathVariable
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiError> handleConstraintViolationException(
      ConstraintViolationException ex) {
    List<FieldErrorResponse> errors =
        ex.getConstraintViolations().stream()
            .map(cv -> new FieldErrorResponse(cv.getPropertyPath().toString(), cv.getMessage()))
            .toList();

    return ResponseEntity.badRequest().body(new ApiError(HttpStatus.BAD_REQUEST, errors));
  }

  // Missing required params
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ApiError> handleMissingParams(MissingServletRequestParameterException ex) {
    List<FieldErrorResponse> errors =
        List.of(
            new FieldErrorResponse(ex.getParameterName(), "Required request parameter is missing"));
    return ResponseEntity.badRequest().body(new ApiError(HttpStatus.BAD_REQUEST, errors));
  }

  // Type mismatch
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
    String type = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown";
    String value = ex.getValue() != null ? ex.getValue().toString() : "null";

    List<FieldErrorResponse> errors =
        List.of(
            new FieldErrorResponse(
                ex.getName(), String.format("Value '%s' is not of required type %s", value, type)));

    return ResponseEntity.badRequest().body(new ApiError(HttpStatus.BAD_REQUEST, errors));
  }

  // Malformed or missing request body
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiError> handleInvalidFormat(HttpMessageNotReadableException ex) {
    List<FieldErrorResponse> errors = new ArrayList<>();
    Throwable cause = ex.getCause();

    if (cause instanceof InvalidFormatException ife
        && ife.getTargetType().isAssignableFrom(LocalDate.class)) {
      errors.add(new FieldErrorResponse("date", "Invalid date format. Please use 'YYYY-MM-DD'."));
    } else if (cause == null) {
      errors.add(new FieldErrorResponse("body", "Request body is missing"));
    } else {
      errors.add(new FieldErrorResponse("body", "Malformed request body"));
    }

    return ResponseEntity.badRequest().body(new ApiError(HttpStatus.BAD_REQUEST, errors));
  }

  // Unsupported HTTP method
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ApiError> handleMethodNotSupported(
      HttpRequestMethodNotSupportedException ex) {
    String message =
        String.format(
            "HTTP method '%s' is not supported for this endpoint. Supported methods: %s",
            ex.getMethod(),
            ex.getSupportedHttpMethods() != null
                ? ex.getSupportedHttpMethods().toString()
                : "none");

    List<FieldErrorResponse> errors = List.of(new FieldErrorResponse("method", message));

    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
        .body(new ApiError(HttpStatus.METHOD_NOT_ALLOWED, errors));
  }

  // Application level
  @ExceptionHandler(AppException.class)
  public ResponseEntity<ApiError> handleAppException(AppException ex) {
    List<FieldErrorResponse> errors =
        List.of(new FieldErrorResponse(ex.getField(), ex.getMessage()));

    return ResponseEntity.badRequest().body(new ApiError(HttpStatus.BAD_REQUEST, errors));
  }

  // Authentication
  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ApiError> handleAuthException(AuthenticationException ignored) {
    List<FieldErrorResponse> errors =
        List.of(new FieldErrorResponse("credentials", "Invalid username or password"));

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(new ApiError(HttpStatus.UNAUTHORIZED, errors));
  }

  // Fallback
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleGenericException(Exception ex) {
    List<FieldErrorResponse> errors =
        List.of(new FieldErrorResponse("error", "Internal server error"));

    // log.error("Unexpected error", ex);
    return ResponseEntity.internalServerError()
        .body(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, errors));
  }
}
