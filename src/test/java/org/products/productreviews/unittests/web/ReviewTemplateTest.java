package org.products.productreviews.unittests.web;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.jupiter.api.Test;
import org.products.productreviews.app.entities.Product;
import org.products.productreviews.app.entities.Review;
import org.products.productreviews.app.repositories.AccountRepository;
import org.products.productreviews.app.repositories.ProductRepository;
import org.products.productreviews.app.repositories.ReviewRepository;
import org.products.productreviews.unittests.security.TestSecurityConfig;
import org.products.productreviews.web.rest.ReviewAPI;
import org.products.productreviews.web.rest.ReviewTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.security.InvalidKeyException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReviewTemplateTest {

    @MockBean
    private SecurityFilterChain securityFilterChain; //do not remove, required to disable security for this test

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    ProductRepository productRepository;

    /**
     * Tests the template that is returned when requesting the current user's reviews
     */
    @Test
    void testShowOwnReviews(){
        String template = restTemplate.getForObject("/review/owner", String.class);
        String expected = "<div class=\"d-flex flex-column container review-container mt-5 p-2\">";
        Pattern pattern = Pattern.compile(expected);

        Matcher matcher = pattern.matcher(template);
        assertTrue(matcher.find());
    }

    /**
     * Tests the template that is returned when requesting reviews for a product name
     * Looks for specific HTML elements
     */
    @Test
    void testShowReviewsForProduct() throws InvalidKeyException, InvalidFormatException {
        Pattern starPattern = Pattern.compile("<div class=\"displayStarFill\">★</div>");
        Pattern ownerPattern = Pattern.compile("<div class=\"me-3\">Review by: null</div>");
        Pattern formTitlePattern = Pattern.compile("<h5 class=\"card-title\">Leave a Review for FountainPenTest</h5>");


        Review review = new Review(null, "Body", Review.Star.ONE);
        Product product = Product.createProduct(productRepository,
                "FountainPenTest", 100, "Description", null);
        product.addReview(review);

        when(productRepository.findByName("FountainPenTest")).thenReturn(product);

        String template = restTemplate.getForObject("/review/search?productName=FountainPenTest", String.class);
        Matcher matcher = starPattern.matcher(template);
        assertTrue(matcher.find());

        matcher = ownerPattern.matcher(template);
        assertTrue(matcher.find());

        matcher = formTitlePattern.matcher(template);
        assertTrue(matcher.find());

    }

    /**
     * Tests the template that is returned when requesting reviews for a product name with no reviews
     * Looks for specific HTML elements
     */
    @Test
    void testShowReviewsForProductEmpty() throws InvalidKeyException, InvalidFormatException {
        Pattern emptyStart = Pattern.compile("<div class=\"container row p-4\">");
        Pattern empty = Pattern.compile("EMPTY");
        Pattern emptyContainer = Pattern.compile("<div class=\"card text-center\">");
        Pattern formTitlePattern = Pattern.compile("<h5 class=\"card-title\">Leave a Review for FountainPenTest</h5>");

        Product product = Product.createProduct(productRepository,
                "FountainPenTest", 100, "Description", null);

        when(productRepository.findByName("FountainPenTest")).thenReturn(product);

        String template = restTemplate.getForObject("/review/search?productName=FountainPenTest", String.class);
        Matcher matcher = emptyStart.matcher(template);
        assertTrue(matcher.find());

        matcher = empty.matcher(template);
        assertTrue(matcher.find());

        matcher = emptyContainer.matcher(template);
        assertTrue(matcher.find());

        matcher = formTitlePattern.matcher(template);
        assertTrue(matcher.find());

    }


}
