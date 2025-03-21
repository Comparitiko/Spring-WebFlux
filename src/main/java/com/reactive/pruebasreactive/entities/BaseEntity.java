package com.reactive.pruebasreactive.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class BaseEntity {
  @Id
  protected String id;

  @Field("created_at")
  @CreatedDate
  @JsonProperty("created_at")
  protected LocalDateTime createdAt;

  @Field("updated_at")
  @LastModifiedDate
  @JsonProperty("updated_at")
  protected LocalDateTime updatedAt;

  public BaseEntity() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }
}
