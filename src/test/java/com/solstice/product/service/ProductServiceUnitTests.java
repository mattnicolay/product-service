package com.solstice.product.service;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import com.solstice.product.dao.ProductRepository;
import com.solstice.product.model.Product;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ProductServiceUnitTests {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @MockBean
  private ProductRepository productRepository;
  private ProductService productService;

  @Before
  public void setup(){
    productService = new ProductService(productRepository);
  }

  @Test
  public void getProducts_Found_ReturnsListOfProducts() {
    when(productRepository.findAll()).thenReturn(getTestProductList());
    List<Product> products = productService.getProducts();

    assertThat(products, is(notNullValue()));
    assertFalse(products.isEmpty());
    products.forEach(product -> assertThatProductsAreEqual(getTestProduct(), product));
  }

  @Test
  public void getProducts_NotFound_ReturnsEmptyList() {
    List<Product> products = productService.getProducts();

    assertThat(products, is(notNullValue()));
    assertTrue(products.isEmpty());
  }

  @Test
  public void getProductById_ValidId_ReturnsProduct() {
    Product testProduct = getTestProduct();
    when(productRepository.findById(1)).thenReturn(testProduct);
    assertThatProductsAreEqual(testProduct, productService.getProductById(1));
  }

  @Test
  public void getProductById_InvalidId_ReturnsNull() {
    assertThat(productService.getProductById(-1), is(nullValue()));
  }

  @Test
  public void createProduct_ValidJson_ReturnsProduct() {
    assertThatProductsAreEqual(getTestProduct(), productService.createProduct(getTestProduct()));
  }

  @Test
  public void updateProduct_ValidIdAndJson_ReturnsProduct() throws IOException {
    when(productRepository.findById(1)).thenReturn(getTestProduct());
    assertThatProductsAreEqual(getTestProduct(), productService.updateProduct(1, getTestProduct()));
  }

  @Test
  public void updateProduct_InvalidId_ReturnsNull() throws IOException {
    assertThat(productService.updateProduct(-1, getTestProduct()), is(nullValue()));
  }

  @Test
  public void deleteProduct_ValidId_ReturnsProduct() {
    Product testProduct = getTestProduct();
    when(productRepository.findById(1)).thenReturn(testProduct);
    assertThatProductsAreEqual(testProduct, productService.deleteProduct(1));
  }

  @Test
  public void deleteProduct_InvalidId_ReturnsNull() {
    assertThat(productService.deleteProduct(1), is(nullValue()));
  }


  private void assertThatProductsAreEqual(Product expected, Product actual) {
    assertThat(actual, is(notNullValue()));
    assertThat(actual.getName(), is(notNullValue()));
    assertThat(actual.getName(), is(equalTo(expected.getName())));
    assertThat(actual.getDescription(), is(notNullValue()));
    assertThat(actual.getDescription(), is(equalTo(expected.getDescription())));
    assertThat(actual.getImage(), is(notNullValue()));
    assertThat(actual.getImage(), is(equalTo(expected.getImage())));
    assertThat(actual.getPrice(), is(notNullValue()));
    assertThat(actual.getPrice(), is(expected.getPrice()));
  }

  private Product getTestProduct() {
    return new Product("Test", "Test", "TestImage", 1.50);
  }

  private List<Product> getTestProductList() {
    List<Product> products = new ArrayList<>();
    products.add(getTestProduct());
    products.add(getTestProduct());

    return products;
  }
}