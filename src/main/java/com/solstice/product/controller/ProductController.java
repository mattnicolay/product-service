package com.solstice.product.controller;

import com.solstice.product.model.Product;
import com.solstice.product.service.ProductService;
import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

  private ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping
  public ResponseEntity<List<Product>> getProducts() {
    List<Product> products = productService.getProducts();
    return new ResponseEntity<>(
        products,
        new HttpHeaders(),
        products.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK
    );
  }

  @GetMapping("/{id}")
  public ResponseEntity<Product> getProductById(@PathVariable("id") long id) {
    Product product = productService.getProductById(id);
    return new ResponseEntity<>(
        product,
        new HttpHeaders(),
        product == null ? HttpStatus.NOT_FOUND : HttpStatus.OK
    );
  }

  @PostMapping
  public ResponseEntity<Product> createProduct(@RequestBody Product body) {
    Product product = productService.createProduct(body);
    return new ResponseEntity<>(
        product,
        new HttpHeaders(),
        product == null ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.CREATED
    );
  }

  @PutMapping("/{id}")
  public ResponseEntity<Product> updateProduct(@PathVariable("id") long id, @RequestBody Product body) {
    Product product = productService.updateProduct(id, body);
    return new ResponseEntity<>(
        product,
        new HttpHeaders(),
        product == null ? HttpStatus.NOT_FOUND : HttpStatus.OK
    );
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Product> deleteProduct(@PathVariable("id") long id) {
    Product product = productService.deleteProduct(id);
    return new ResponseEntity<>(
        product,
        new HttpHeaders(),
        product == null ? HttpStatus.NOT_FOUND : HttpStatus.OK
    );
  }
}
