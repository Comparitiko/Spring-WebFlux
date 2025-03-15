package com.reactive.pruebasreactive.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductDto(
  @NotNull(message = "The name is required")
  @NotBlank(message = "The name cannot be empty")
  String name,

  @NotNull(message = "The price is required")
  @NotBlank(message = "The price cannot be empty")
  Double price
) {

}
