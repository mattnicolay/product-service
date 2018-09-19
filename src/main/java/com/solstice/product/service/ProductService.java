package com.solstice.product.service;

import com.solstice.product.dao.ProductRepository;
import com.solstice.product.model.Product;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

  private ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public List<Product> getProducts() {
    return productRepository.findAll();
  }

  public Product getProductById(long id) {
    return productRepository.findById(id);
  }

  public Product createProduct(Product product) {
    if (product != null) {
      productRepository.save(product);
    }
    return product;
  }

  public Product updateProduct(long id, Product product) {
    Product dbProduct = productRepository.findById(id);
    if (dbProduct == null || product == null) {
      return null;
    }
    product.setId(id);
    productRepository.save(product);

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
