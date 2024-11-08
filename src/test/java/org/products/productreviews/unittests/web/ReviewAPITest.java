package org.products.productreviews.unittests.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.products.productreviews.app.entities.Review;
import org.products.productreviews.app.repositories.ProductRepository;
import org.products.productreviews.app.repositories.ReviewRepository;
import org.products.productreviews.web.rest.ReviewAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.Mockito.when;

@WebMvcTest(ReviewAPI.class)
class ReviewAPITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReviewRepository reviewRepository;

    //TODO: Delete when CommandlineRunner is deleted
    @MockBean
    ProductRepository productRepository;

    /**
     * Tests editing existing reviews
     */
    @Test
    void testEditExistingReview() throws Exception {
        Review review = new Review(null, "Hello", Review.Star.ONE);
        when(reviewRepository.findById(0L)).thenReturn(Optional.of(review));

        Review newReview = new Review(null, "Not Hello", Review.Star.TWO);
        String pathBody = objectMapper.writeValueAsString(newReview);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/review/edit")
                        .contentType("application/json")
                        .content(pathBody))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().string(pathBody))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    /**
     * Tests Editing a non-existing review
     */
    @Test
    void testEditNonExistingReview() throws Exception {
        Review newReview = new Review(null, "RevBody", Review.Star.THREE);
        String pathBody = objectMapper.writeValueAsString(newReview);

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/review/edit")
                                .contentType("application/json")
                                .content(pathBody))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().string(pathBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Tests deleting an existing Review
     */
    @Test
    void testDeleteExistingReview() throws Exception {
        Review review = new Review(null, "Hello", Review.Star.ONE);
        when(reviewRepository.findById(0L)).thenReturn(Optional.of(review));

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/review/delete/0"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Tests Deleting a non-existing Review
     */
    @Test
    void testDeleteNonExistingReview() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/review/delete/0"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}