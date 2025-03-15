package com.reactive.pruebasreactive.responses;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Getter
public class ErrorResponse extends Response {
  private final String message;

  public ErrorResponse(HttpStatus status, String message) {
    super(status);
    this.message = message;
  }
}
