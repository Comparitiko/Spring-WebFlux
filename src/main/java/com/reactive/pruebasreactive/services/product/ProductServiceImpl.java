package com.reactive.pruebasreactive.services.product;

import com.reactive.pruebasreactive.dtos.ProductDto;
import com.reactive.pruebasreactive.entities.Product;
import com.reactive.pruebasreactive.exceptions.CustomException;
import com.reactive.pruebasreactive.exceptions.CustomValidationException;
import com.reactive.pruebasreactive.repositories.ProductRepository;
import com.reactive.pruebasreactive.responses.DataResponse;
import com.reactive.pruebasreactive.responses.FieldError;
import com.reactive.pruebasreactive.responses.PaginatedResponse;
import com.reactive.pruebasreactive.responses.Response;
import com.reactive.pruebasreactive.utils.ParamsUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    return productDB.flatMap(product -> Response.builder(HttpStatus.OK)
      .bodyValue(new DataResponse<>(HttpStatus.OK, product)
    ));
  }

  @Override
  public Mono<ServerResponse> createProduct(ServerRequest request) {

    Mono<ProductDto> product = request.bodyToMono(ProductDto.class);

    // Validate the product DTO using the validator
    Mono<Product> validatedProduct = product.flatMap(p -> {
      Set<ConstraintViolation<ProductDto>> violations = this.validator.validate(p);
      if (!violations.isEmpty()) {
        // Convertir las violaciones en un mapa de errores detallados
        List<FieldError> errorList = violations.stream()
          .map(v -> new FieldError(v.getPropertyPath().toString(), v.getMessage()))
          .toList();
        return Mono.error(new CustomValidationException("Invalid product", errorList));
      }
      // If the product is valid, create a new Product using the ProductDto
      return Mono.just(new Product(p));
    });

    Mono<Product> savedProduct = validatedProduct.flatMap(p ->
      this.productRepository.existsByName(p.getName())
        .flatMap(exists -> {
          if (exists) {
            return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Product already exists"));
          }
          // Guardar el producto antes de responder
          return this.productRepository.save(p);
        })
    );

    return savedProduct.flatMap(pr -> Response.builder(HttpStatus.CREATED)
      .bodyValue(new DataResponse<>(HttpStatus.CREATED, pr))
    );
  }
}
