package com.solstice.product.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solstice.product.dao.ProductRepository;
import com.solstice.product.model.Product;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

  private ProductRepository productRepository;
  private ObjectMapper objectMapper;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
    objectMapper = new ObjectMapper();
  }

  public List<Product> getProducts() {
    return productRepository.findAll();
  }

  public Product getProductById(long id) {
    return productRepository.findById(id);
  }

  public Product createProduct(String data) throws IOException {
    Product product = objectMapper.readValue(data, Product.class);
    if (product != null) {
      productRepository.save(product);
    }
    return product;
  }

  public Product updateProduct(long id, String data) throws IOException {
    Product product = null;
    Product dbProduct = productRepository.findById(id);
    if (dbProduct != null) {
      product = objectMapper.readValue(data, Product.class);
      product.setId(id);
      productRepository.save(product);
    }
    return product;
  }

  public Product deleteProduct(long id) {
    Product product = productRepository.findById(id);
    if (product != null) {
      productRepository.delete(product);
    }
    return product;
  }
}
