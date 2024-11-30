package org.products.productreviews.unittests.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.products.productreviews.app.entities.Account;
import org.products.productreviews.app.entities.Product;
import org.products.productreviews.app.entities.Review;
import org.products.productreviews.app.repositories.AccountRepository;
import org.products.productreviews.app.repositories.ProductRepository;
import org.products.productreviews.app.repositories.ReviewRepository;
import org.products.productreviews.web.rest.ReviewAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@WebMvcTest(ReviewAPI.class)
@WithMockUser("User100")
class ReviewAPITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReviewRepository reviewRepository;

    @MockBean
    ProductRepository productRepository;

    @MockBean
    AccountRepository accountRepository;

    /**
     * Tests editing existing reviews
     */
    @Test
    void testEditExistingReviewJSON() throws Exception {
        Review review = new Review(null, "Hello", Review.Star.ONE);
        when(reviewRepository.findById(0L)).thenReturn(Optional.of(review));

        Review newReview = new Review(null, "Not Hello", Review.Star.TWO);
        String pathBody = objectMapper.writeValueAsString(newReview);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/review/editJSON")
                        .contentType("application/json")
                        .content(pathBody)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(pathBody));

    }

    /**
     * Tests Editing a non-existing review
     */
    @Test
    void testEditNonExistingReviewJSON() throws Exception {
        Review newReview = new Review(null, "RevBody", Review.Star.THREE);
        String pathBody = objectMapper.writeValueAsString(newReview);

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/review/editJSON")
                                .contentType("application/json")
                                .content(pathBody)
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().string(pathBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Tests Editing a review
     */
    @Test
    void testEditExistingProduct() throws Exception {
        Account account = Account.createAccount(accountRepository, "User100", "Pass1234!");
        Product product = Product.createProduct(productRepository,
                "product1",
                399.99f,
                "product1",
                null,
                null);
        Review review = new Review(account, "Body", Review.Star.THREE);

        account.addReview(review);
        product.addReview(review);
        review.setProduct(product);

        when(accountRepository.findByUsername("User100")).thenReturn(Optional.of(account));
        when(productRepository.findByName("product1")).thenReturn(product);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/review/product1/edit")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("rating", "5")
                        .param("body", "New!")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertEquals("New!", review.getBody());
        assertEquals(Review.Star.FIVE, review.getRating());

    }

    /**
     * Tests deleting an existing Review
     */
    @Test
    void testDeleteExistingReviewJSON() throws Exception {
        Review review = new Review(null, "Hello", Review.Star.ONE);
        when(reviewRepository.findById(0L)).thenReturn(Optional.of(review));

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/review/delete/0")
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Tests Deleting a non-existing Review
     */
    @Test
    void testDeleteNonExistingReviewJSON() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/review/delete/0")
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}