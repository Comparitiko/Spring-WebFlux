package com.reactive.pruebasreactive.exceptions;

import com.reactive.pruebasreactive.responses.FieldError;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class CustomValidationException extends RuntimeException {
  private final HttpStatus status = HttpStatus.BAD_REQUEST;
  private final List<FieldError> errors;

  public CustomValidationException(String message, List<FieldError> errors) {
    super(message);
    this.errors = errors;
  }
}
