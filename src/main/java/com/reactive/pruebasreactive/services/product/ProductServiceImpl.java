package com.reactive.pruebasreactive.services.product;

import com.reactive.pruebasreactive.dtos.ProductDto;
import com.reactive.pruebasreactive.entities.Product;
import com.reactive.pruebasreactive.exceptions.CustomException;
import com.reactive.pruebasreactive.repositories.ProductRepository;
import com.reactive.pruebasreactive.responses.DataResponse;
import com.reactive.pruebasreactive.responses.PaginatedResponse;
import com.reactive.pruebasreactive.responses.Response;
import com.reactive.pruebasreactive.utils.ParamsUtils;
import io.netty.channel.unix.Errors;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@Service
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final Validator validator;

  public ProductServiceImpl(ProductRepository productRepository, Validator validator) {
    this.productRepository = productRepository;
    this.validator = validator;
  }

  @Override
  public Mono<ServerResponse> getAllProducts(ServerRequest request) {
    // Retrieve the page and limit parameters from the request
    String page = request.queryParam("page").orElse("1");
    String limit = request.queryParam("limit").orElse("10");

    // Convert the page and limit to integers
    Integer pageNumber = ParamsUtils.checkPage(page);
    Integer limitNumber = ParamsUtils.checkLimit(limit);

    PageRequest pageRequest = PageRequest.of(pageNumber, limitNumber);

    // Retrieve the products from the database
    Flux<Product> products = productRepository.findAllBy(pageRequest);

    return products.collectList()
      .zipWith(productRepository.count())
      .flatMap( tuple -> Response.builder(HttpStatus.OK)
        .bodyValue(new PaginatedResponse<>(
          HttpStatus.OK,
          tuple.getT2(),
          pageRequest,
          limitNumber,
          tuple.getT1()
        ))
      );
  }

  @Override
  public Mono<ServerResponse> getProductById(ServerRequest request) {
    String objectId = request.pathVariable("id");

    // Validar que el ID no esté vacío
    if (objectId.isBlank() || objectId.equals("asd")) {
      return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "The id cannot be empty"));
    }

    // Try to find the product by ID if it exists
    Mono<Product> productDB = productRepository.findById(objectId)
      .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Product not found")));

    return productDB.flatMap(product -> ServerResponse.ok()
      .bodyValue(new DataResponse<>(HttpStatus.OK, product)
    ));
  }

  @Override
  public Mono<ServerResponse> createProduct(ServerRequest request) {
//    Mono<Product> product = request.bodyToMono(ProductDto.class)
//      .flatMap(productDto -> {
//        Errors errors = new BeanPropertyBindingResult(productDto, "productDto");
//      }
//      };
//
//    return ServerResponse.status(HttpStatus.CREATED)
//      .contentType(MediaType.APPLICATION_JSON)
//      .body(this.productRepository.saveAll(product), Product.class);

    return ServerResponse.ok().bodyValue("ok");
  }

  @Override
  public Mono<ServerResponse> updateProduct(ServerRequest request) {
    return ServerResponse.ok().bodyValue("ok");
  }

  @Override
  public Mono<ServerResponse> deleteProduct(ServerRequest request) {
    return ServerResponse.ok().bodyValue("ok");
  }
}
