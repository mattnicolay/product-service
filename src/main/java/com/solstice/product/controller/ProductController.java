package com.solstice.product.controller;

import com.solstice.product.exception.HTTP400Exception;
import com.solstice.product.exception.HTTP404Exception;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController extends AbstractRestController{

  private ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping
  public @ResponseBody List<Product> getProducts() {
    List<Product> products = productService.getProducts();
    if(products.isEmpty()) {
      throw new HTTP404Exception("Resource not found");
    }
    return AbstractRestController.checkResourceFound(products);
  }

  @GetMapping("/{id}")
  public @ResponseBody Product getProductById(@PathVariable("id") long id) {
    Product product = productService.getProductById(id);
    return AbstractRestController.checkResourceFound(product);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public @ResponseBody Product createProduct(@RequestBody Product body) {
    Product product = productService.createProduct(body);
    if (product == null) {
      throw new HTTP400Exception("Could not create product");
    }
    return product;
  }

  @PutMapping("/{id}")
  public @ResponseBody Product updateProduct(@PathVariable("id") long id, @RequestBody Product body) {
    Product product = productService.updateProduct(id, body);
    return AbstractRestController.checkResourceFound(product);
  }

  @DeleteMapping("/{id}")
  public @ResponseBody Product deleteProduct(@PathVariable("id") long id) {
    Product product = productService.deleteProduct(id);
    return AbstractRestController.checkResourceFound(product);
  }
}
