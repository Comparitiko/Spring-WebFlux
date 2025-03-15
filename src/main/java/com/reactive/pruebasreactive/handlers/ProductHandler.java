package com.reactive.pruebasreactive.handlers;

import com.reactive.pruebasreactive.entities.Product;
import com.reactive.pruebasreactive.responses.DataResponse;
import com.reactive.pruebasreactive.services.product.ProductService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class ProductHandler {
  private final ProductService productService;

  public ProductHandler(ProductService productService) {
    this.productService = productService;
  }

  public Mono<ServerResponse> getAllProducts(ServerRequest request) {
    return productService.getAllProducts(request);
  }

  public Mono<ServerResponse> getProductById(ServerRequest request) {
    return productService.getProductById(request);
  }

  public Mono<ServerResponse> createProduct(ServerRequest request) {
    return productService.createProduct(request);
  }

  public Mono<ServerResponse> updateProduct(ServerRequest request) {
    return productService.updateProduct(request);
  }

  public Mono<ServerResponse> deleteProduct(ServerRequest request) {
    return productService.deleteProduct(request);
  }
}
