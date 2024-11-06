package org.products.productreviews.unittests.web;

import org.junit.jupiter.api.Test;
import org.products.productreviews.app.entities.Product;
import org.products.productreviews.app.repositories.ProductRepository;
import org.products.productreviews.web.rest.ProductAPI;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

@WebMvcTest(ProductAPI.class)
public class ProductAPITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductRepository productRepository;

    @Test
    void testProductDetailsFound() throws Exception {
        Product product = Product.createProduct(productRepository, "Test Product", 100f, "Test Description",  "test.jpg");
        //TODO: do we need to set an ID?
        productRepository.save(product);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/product/1"))
                .andDo(print())
                .andExpect(status().isOk());
//                .andExpect(content().string(containsString("Test Product")))
//                .andExpect(content().string(containsString("100")))
//                .andExpect(content().string(containsString("Test Description")));
    }





}
