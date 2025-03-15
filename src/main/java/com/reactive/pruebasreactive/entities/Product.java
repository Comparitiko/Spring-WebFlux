package com.reactive.pruebasreactive.entities;

import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Product extends BaseEntity {
  @Indexed(unique = true)
  private String name;

  private Double price;

  public Product(String name, Double price) {
    super();
    this.name = name;
    this.price = price;
  }
}
