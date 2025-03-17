package com.reactive.pruebasreactive.exceptions;

import com.reactive.pruebasreactive.responses.ErrorResponse;
import com.reactive.pruebasreactive.responses.Response;
import com.reactive.pruebasreactive.responses.ValidationErrorResponse;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.resource.NoResourceFoundException;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(CustomException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleCustomException(CustomException e){
    logger.error("CustomException: {}", e.getMessage());
    ErrorResponse errorResponse = new ErrorResponse(e.getStatus(), e.getMessage());

    return Mono.just(ResponseEntity.status(e.getStatus())
      .body(errorResponse));
  }

  @ExceptionHandler(CustomValidationException.class)
  public Mono<ResponseEntity<ValidationErrorResponse>> handleValidationExceptions(CustomValidationException ex) {
    ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse(
      HttpStatus.BAD_REQUEST,
      ex.getMessage(),
      ex.getErrors()
    );

    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationErrorResponse));
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleNoResourceFoundException(NoResourceFoundException e) {
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND,  "Resource not found");
    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
      .body(errorResponse));
  }

  @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleNotFoundException(ChangeSetPersister.NotFoundException ex) {
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND,  "Resource not found");
    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
      .body(errorResponse));
  }

  @ExceptionHandler(RuntimeException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleException(Exception e){
    logger.error("RuntimeException: {}", e.getMessage());
    var errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(errorResponse));
  }

}
