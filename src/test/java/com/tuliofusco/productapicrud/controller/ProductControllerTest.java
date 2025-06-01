package com.tuliofusco.productapicrud.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuliofusco.productapicrud.model.Category;
import com.tuliofusco.productapicrud.model.Product;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    void testAddProduct() throws Exception {
        Product product = new Product();
        product.setName("Produto Teste");
        product.setDescription("Produto teste para o endpoint");
        product.setPrice(BigDecimal.valueOf(99.99));
        product.setQuantityStock(10);
        product.setCategory(Category.ELECTRONICS);
        product.setActive(true);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk());
    }

}
