package com.solstice.product.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solstice.product.model.Product;
import com.solstice.product.service.ProductService;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductController.class)
public class ProductControllerUnitTests {

  private Logger logger = LoggerFactory.getLogger(this.getClass());
  private final String GET = "GET";
  private final String POST = "POST";
  private final String PUT = "PUT";
  private final String DELETE = "DELETE";

  @MockBean
  private ProductService productService;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    ProductController productController  = new ProductController(productService);
    mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
  }

  @Test
  public void getProducts_Found_Code200ReturnsListOfProducts() throws Exception {
    List<Product> products = getTestProductList();
    when(productService.getProducts()).thenReturn(products);
    mockMvcPerform(GET, "/products", "",200, toJson(products));
  }

  @Test
  public void getProducts_NotFound_Code404EmptyResponse() throws Exception {
    mockMvcPerform(GET, "/products", "",404, "Resource not found");
  }

  @Test
  public void getProductById_ValidId_Code200ReturnsProduct() throws Exception {
    Product product = getTestProduct();
    when(productService.getProductById(1)).thenReturn(product);
    mockMvcPerform(GET, "/products/1", "",200, toJson(product));
  }

  @Test
  public void getProductById_InvalidId_Code404EmptyResponse() throws Exception {
    mockMvcPerform(GET, "/products/1", "",404, "Resource not found");
  }

  @Test
  public void createProduct_ValidJson_Code201ReturnsProduct() throws Exception {
    Product product = getTestProduct();
    when(productService.createProduct(any(Product.class))).thenReturn(product);
    mockMvcPerform(POST, "/products", toJson(product),201, toJson(product));
  }

  @Test
  public void createProduct_EmptyBody_Code400EmptyResponse() throws Exception {
    mockMvcPerform(POST, "/products", "",400, "");
  }

  @Test
  public void updateProduct_ValidIdAndJson_Code200ReturnsProduct() throws Exception {
    Product product = getTestProduct();
    String json = toJson(product);
    when(productService.updateProduct(anyLong(), any(Product.class))).thenReturn(product);
    mockMvcPerform(PUT, "/products/1", json, 200, json);
  }

  @Test
  public void updateProduct_InvalidId_Code404EmptyResponse() throws Exception {
    mockMvcPerform(PUT, "/products/-1", toJson(getTestProduct()), 404, "Resource not found");
  }

  @Test
  public void updateProduct_EmptyBody_Code400EmptyResponse() throws Exception {
    mockMvcPerform(PUT, "/products/1", "", 400, "");
  }

  @Test
  public void deleteProduct_ValidId_Code200ReturnsProduct() throws Exception {
    Product product = getTestProduct();
    when(productService.deleteProduct(1)).thenReturn(product);
    logger.info(toJson(product));
    mockMvcPerform(DELETE, "/products/1", "", 200, toJson(product));
  }

  @Test
  public void deleteProduct_InvalidId_Code404EmptyResponse() throws Exception {
    mockMvcPerform(DELETE, "/products/-1", "", 404, "Resource not found");
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

  private String toJson(Object object) {
    ObjectMapper objectMapper = new ObjectMapper();
    String result = null;
    try {
      result = objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      logger.error("JsonProcessingException thrown: {}", e.toString());
    }
    return result;
  }

  private void mockMvcPerform(String method, String endpoint, String requestBody, int expectedStatus,
      String expectedResponseBody) throws Exception {
    switch(method){

      case GET:
        mockMvc.perform(get(endpoint)).andExpect(status().is(expectedStatus))
            .andExpect(content().string(expectedResponseBody));
        break;

      case POST:
        mockMvc.perform(
            post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        ).andExpect(status().is(expectedStatus))
            .andExpect(content().string(expectedResponseBody));
        break;

      case PUT:
        mockMvc.perform(
            put(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        ).andExpect(status().is(expectedStatus))
            .andExpect(content().string(expectedResponseBody));
        break;

      case DELETE:
        mockMvc.perform(delete(endpoint)).andExpect(status().is(expectedStatus))
            .andExpect(content().string(expectedResponseBody));
        break;

      default:
        logger.error("Unknown method '{}' given to mockMvcPerform", method);
    }
  }
}