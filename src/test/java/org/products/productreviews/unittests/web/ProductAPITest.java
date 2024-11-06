package org.products.productreviews.unittests.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.products.productreviews.app.entities.Account;
import org.products.productreviews.app.entities.Product;
import org.products.productreviews.app.entities.Review;
import org.products.productreviews.app.repositories.AccountRepository;
import org.products.productreviews.app.repositories.ProductRepository;
import org.products.productreviews.app.repositories.ReviewRepository;
import org.products.productreviews.web.rest.ProductAPI;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

/**
 * Unit tests for @ProductAPI controller
 */
@WebMvcTest(ProductAPI.class)
public class ProductAPITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private ReviewRepository reviewRepository;

    @Autowired
    private ObjectMapper objectMapper;

    //No @BeforeEach function with the entities instantiated because the create methods throw errors

    /**
     * Verifies that the end point /product/{id} returns the product details associated with the given id
     * @throws Exception if an error occurs during the request
     */
    @Test
    void testProductDetailsFound() throws Exception {
        Product product = Product.createProduct(productRepository, "Test Product", 100f, "Test Description",  "test.jpg");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/product/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.price").value(100f))
                .andExpect(jsonPath("$.description").value("Test Description"));
    }

    /**
     * Verifies that the end point /product/{id} returns the appropriate error message when given an invalid product id
     * @throws Exception if an error occurs during the request
     */
    @Test
    void testProductDetailsNotFound() throws Exception {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/product/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Product with id = 1 not found")));

    }

    /**
     * Verifies that the endpoint /product/{id}/reviews returns the list of reviews associated with the given product id
     * @throws Exception if an error occurs during the request
     */
    @Test
    void testDisplayProductReviews() throws Exception {
        Product product = Product.createProduct(productRepository, "Test Product", 100f, "Test Description",  "test.jpg");
        Account account = Account.createAccount(accountRepository, "testUser", "pass1");
        Review review = new Review(account, "Test Review Description", Review.Star.FIVE);
        product.addReview(review);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/product/1/reviews"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Test Review Description")))
                .andExpect(content().string(containsString("FIVE")));
    }

    /**
     * Verifies that the endpoint /product/{id}/reviews returns the appropriate error message when given an invalid product id
     * @throws Exception if an error occurs during the request
     */
    @Test
    void testDisplayProductEmptyReviews() throws Exception {
        Product product = Product.createProduct(productRepository, "Test Product", 100f, "Test Description",  "test.jpg");
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/product/1/reviews"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    /**
     * Verifies that the endpoint /product/{id}/submitReview successfully adds a review entity to the given product
     * @throws Exception if an error occurs during the request
     */
    @Test
    void testSubmitValidReview() throws Exception {
        Product product = Product.createProduct(productRepository, "Test Product", 100f, "Test Description",  "test.jpg");
        Account account = Account.createAccount(accountRepository, "testUser", "pass1");
        Review review = new Review(account, "Test Review Description", Review.Star.FIVE);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(reviewRepository.findById(0L)).thenReturn(Optional.of(review)); //needed to set to 0, I think because Review does not have auto generated ID yet


        String reviewJson = objectMapper.writeValueAsString(review);

        mockMvc.perform(post("/product/1/submitReview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reviewJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Review added successfully"));
    }

    /**
     * Verifies that the endpoint /product/{id}/submitReview returns appropriate error message when the given product id is invalid
     * @throws Exception if an error occurs during the request
     */
    @Test
    void testSubmitInvalidReview() throws Exception {
        Account account = Account.createAccount(accountRepository, "testUser", "pass1");
        Review review = new Review(account, "Test Review Description", Review.Star.FIVE);

        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        //when(reviewRepository.findById(0L)).thenReturn(Optional.of(review)); //TODO: add when createReview is added

        String reviewJson = objectMapper.writeValueAsString(review);

        mockMvc.perform(post("/product/1/submitReview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reviewJson))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Product with id = 1 not found")));
    }

}
