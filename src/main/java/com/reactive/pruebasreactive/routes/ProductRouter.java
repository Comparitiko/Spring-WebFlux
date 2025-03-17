package com.reactive.pruebasreactive.routes;

import com.reactive.pruebasreactive.handlers.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class ProductRouter {

  private final ProductHandler handler;

  public ProductRouter(ProductHandler handler) {
    this.handler = handler;
  }

  @Bean
  public RouterFunction<ServerResponse> productRoutes() {
    return RouterFunctions.route()
      .GET("", handler::getAllProducts) // Route: GET /api/products
      .GET("/{id}", handler::getProductById) // Route: GET /api/products/{id}
      .POST("", handler::createProduct) // Route: POST /api/products
      .PUT("/{id}", handler::updateProduct) // Route: PUT /api/products/{id}
      .DELETE("/{id}", handler::deleteProduct) // Route: DELETE /api/products/{id}
      .build();
  }
}
