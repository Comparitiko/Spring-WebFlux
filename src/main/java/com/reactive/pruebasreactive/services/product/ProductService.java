package com.reactive.pruebasreactive.services.product;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public interface ProductService {
  Mono<ServerResponse> getAllProducts(ServerRequest request);
  Mono<ServerResponse> getProductById(ServerRequest request);
  Mono<ServerResponse> createProduct(ServerRequest request);
}
