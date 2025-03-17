package com.reactive.pruebasreactive.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record ProductDto(
  @NotNull(message = "The name is required")
  @Length(min = 1, message = "The name must be at least 1 character")
  String name,

  @NotNull(message = "The price is required")
  @Min(value = 2, message = "The price must be greater than 2")
  Double price
) {

}
