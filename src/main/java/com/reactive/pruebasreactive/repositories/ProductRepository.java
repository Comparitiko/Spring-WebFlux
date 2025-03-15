package com.reactive.pruebasreactive.repositories;

import com.reactive.pruebasreactive.entities.Product;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
  Flux<Product> findAllBy(PageRequest pageRequest);

  Mono<Boolean> existsByName(String name);
}
