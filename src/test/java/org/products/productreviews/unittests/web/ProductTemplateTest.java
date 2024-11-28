package org.products.productreviews.unittests.web;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.jupiter.api.Test;
import org.products.productreviews.app.entities.Product;
import org.products.productreviews.app.entities.Review;
import org.products.productreviews.app.repositories.ProductRepository;
import org.products.productreviews.web.util.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.security.web.SecurityFilterChain;

import java.security.InvalidKeyException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductTemplateTest {

    @MockBean
    private SecurityFilterChain securityFilterChain; //do not remove, required to disable security for this test

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    ProductRepository productRepository;

    /**
     * Tests the template that is returned when requesting reviews for a product name
     * Looks for specific HTML elements
     */
    @Test
    void testShowReviewsForProduct() throws InvalidKeyException, InvalidFormatException {
        Pattern productName = Pattern.compile("<h5 class=\"card-title\">FountainPenTest</h5>");
        Pattern productDescription = Pattern.compile("<p class=\"card-text\">Description</p>");
        Pattern starPattern = Pattern.compile("<div class=\"displayStarFill\">★</div>");
        Pattern formTitlePattern = Pattern.compile("<h5 class=\"card-title\">Leave a Review for FountainPenTest</h5>");


        Review review = new Review(null, "Body", Review.Star.ONE);
        Product product = Product.createProduct(productRepository,
                "FountainPenTest", 100, "Description", null, ProductCategory.OFFICE_SUPPLIES);
        product.addReview(review);

        when(productRepository.findByName("FountainPenTest")).thenReturn(product);

        String template = restTemplate.getForObject("/product/show?productName=FountainPenTest", String.class);

        Matcher matcher = productName.matcher(template);
        assertTrue(matcher.find());

        matcher = productDescription.matcher(template);
        assertTrue(matcher.find());

        matcher = starPattern.matcher(template);
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
        Pattern productName = Pattern.compile("<h5 class=\"card-title\">FountainPenTest</h5>");
        Pattern productDescription = Pattern.compile("<p class=\"card-text\">Description</p>");
        Pattern emptyStart = Pattern.compile("<div class=\"card text-center ghostwhite-bg-col\">");
        Pattern empty = Pattern.compile("<h4>No Reviews Yet \uD83D\uDE1E</h4>");
        Pattern formTitlePattern = Pattern.compile("<h5 class=\"card-title\">Leave a Review for FountainPenTest</h5>");

        Product product = Product.createProduct(productRepository,
                "FountainPenTest", 100, "Description", null, ProductCategory.OFFICE_SUPPLIES);

        when(productRepository.findByName("FountainPenTest")).thenReturn(product);

        String template = restTemplate.getForObject("/product/show?productName=FountainPenTest", String.class);

        Matcher matcher = productName.matcher(template);
        assertTrue(matcher.find());

        matcher = productDescription.matcher(template);
        assertTrue(matcher.find());

        matcher = emptyStart.matcher(template);
        assertTrue(matcher.find());

        matcher = empty.matcher(template);
        assertTrue(matcher.find());

        matcher = formTitlePattern.matcher(template);
        assertTrue(matcher.find());
    }

}
