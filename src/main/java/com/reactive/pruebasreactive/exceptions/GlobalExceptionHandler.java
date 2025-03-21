package com.reactive.pruebasreactive.exceptions;

import com.reactive.pruebasreactive.exceptions.custom.CustomException;
import com.reactive.pruebasreactive.exceptions.custom.CustomValidationException;
import com.reactive.pruebasreactive.responses.error.ErrorResponse;
import com.reactive.pruebasreactive.responses.Response;
import com.reactive.pruebasreactive.responses.error.ValidationErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.resource.NoResourceFoundException;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(CustomException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleCustomException(CustomException e){
    logger.error("CustomException: {}", e.getMessage());
    ErrorResponse errorResponse = new ErrorResponse(e.getStatus(), e.getMessage());

    return Response.builder(e.getStatus(), errorResponse);
  }

  @ExceptionHandler(CustomValidationException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleValidationExceptions(CustomValidationException ex) {
    ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse(
      HttpStatus.BAD_REQUEST,
      ex.getMessage(),
      ex.getErrors()
    );

    return Response.builder(HttpStatus.BAD_REQUEST, validationErrorResponse);
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleNoResourceFoundException(NoResourceFoundException e) {
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND,  "Resource not found");
    return Response.builder(HttpStatus.NOT_FOUND, errorResponse);
  }

  @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleNotFoundException(ChangeSetPersister.NotFoundException ex) {
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND,  "Resource not found");
    return Response.builder(HttpStatus.NOT_FOUND, errorResponse);
  }

  @ExceptionHandler(RuntimeException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleException(Exception e){
    logger.error("RuntimeException: {}", e.getMessage());
    var errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
    return Response.builder(HttpStatus.INTERNAL_SERVER_ERROR, errorResponse);
  }

}
