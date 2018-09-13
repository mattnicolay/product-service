package com.solstice.product.dao;

import com.solstice.product.model.Product;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {

  List<Product> findAll();

  Product findById(long id);
}
