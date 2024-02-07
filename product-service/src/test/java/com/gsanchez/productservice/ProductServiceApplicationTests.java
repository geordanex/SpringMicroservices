package com.gsanchez.productservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gsanchez.productservice.dto.ProductRequest;
import com.gsanchez.productservice.dto.ProductResponse;
import com.gsanchez.productservice.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductServiceApplicationTests extends MongoContainerInitialiserTest{

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ProductService productService;

	@Test
	void shouldFindAllProducts() throws Exception {
		productService.createProduct(getProductRequest());

		mockMvc.perform(MockMvcRequestBuilders.get("/api/product"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Iphone 13")));
	}

	@Test
	void shouldCreateProduct() throws Exception {
		ProductRequest productRequest = getProductRequest();

		String productRequestString = objectMapper.writeValueAsString(productRequest);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
				.contentType(MediaType.APPLICATION_JSON)
				.content(productRequestString))
				.andExpect(status().isCreated());
		assertEquals(productService.getAllProducts().size(), 2);
	}
	@Test
	void shouldFindAllProductsUsingRepository(){
		productService.createProduct(getProductRequest());
		List<ProductResponse> results = productService.getAllProducts();
		assertEquals(results.get(0).getName(), getProductRequest().getName());
	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("Iphone 13")
				.description("This is a Iphone 13")
				.price(BigDecimal.valueOf(2000))
				.build();
	}

}
