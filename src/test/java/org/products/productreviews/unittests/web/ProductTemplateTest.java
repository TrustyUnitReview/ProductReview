package org.products.productreviews.unittests.web;

import org.junit.jupiter.api.Test;
import org.products.productreviews.app.entities.Account;
import org.products.productreviews.app.entities.Product;
import org.products.productreviews.app.entities.Review;
import org.products.productreviews.app.repositories.AccountRepository;
import org.products.productreviews.app.repositories.ProductRepository;
import org.products.productreviews.web.rest.ProductTemplate;
import org.products.productreviews.web.util.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@WebMvcTest(ProductTemplate.class)
public class ProductTemplateTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ProductRepository productRepository;

    @MockBean
    AccountRepository accountRepository;

    /**
     * Tests the template that is returned when requesting reviews for a product name
     * Looks for specific HTML elements
     */
    @Test
    @WithMockUser
    void testShowReviewsForProduct() throws Exception {
        Pattern productName = Pattern.compile("<h5 class=\"card-title\">FountainPenTest</h5>");
        Pattern productDescription = Pattern.compile("<p class=\"card-text\">Description</p>");
        Pattern starPattern = Pattern.compile("<div class=\"displayStarFill\">★</div>");
        Pattern formTitlePattern = Pattern.compile("<h5 class=\"card-title\">Leave a Review for FountainPenTest</h5>");


        Review review = new Review(null, "Body", Review.Star.ONE);
        Product product = Product.createProduct(productRepository,
                "FountainPenTest", 100, "Description", null, ProductCategory.OFFICE_SUPPLIES);
        product.addReview(review);

        when(productRepository.findByName("FountainPenTest")).thenReturn(product);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/product/show?productName=FountainPenTest"))
                .andReturn();

        String template = result.getResponse().getContentAsString();

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
    @WithMockUser
    void testShowReviewsForProductEmpty() throws Exception {
        Pattern productName = Pattern.compile("<h5 class=\"card-title\">FountainPenTest</h5>");
        Pattern productDescription = Pattern.compile("<p class=\"card-text\">Description</p>");
        Pattern emptyStart = Pattern.compile("<div class=\"card text-center ghostwhite-bg-col\">");
        Pattern empty = Pattern.compile("<h4>No Reviews Yet \uD83D\uDE1E</h4>");
        Pattern formTitlePattern = Pattern.compile("<h5 class=\"card-title\">Leave a Review for FountainPenTest</h5>");

        Product product = Product.createProduct(productRepository,
                "FountainPenTest", 100, "Description", null, ProductCategory.OFFICE_SUPPLIES);

        when(productRepository.findByName("FountainPenTest")).thenReturn(product);

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.get("/product/show?productName=FountainPenTest"))
                .andReturn();

        String template = result.getResponse().getContentAsString();

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

    /**
     * Test if the Jaccard sorting fills the template correctly.
     * <br>
     * <br>
     * Three users are created User100, User101, User102.
     * The test is performed from User100's perspective. The users are set up in a way where
     * User102 is closer to User100 than User101.
     * <br>
     * <br>
     * Review Order should be:
     * <code>User100 (Because you cant get closer than yourself) > User102 > User101</code>
     *
     */
    @Test
    @WithMockUser("User100")
    void testFilterWithJaccardSort() throws Exception {

        Pattern user100Pattern = Pattern.compile("User100");
        Pattern user101Pattern = Pattern.compile("User101");
        Pattern user102Pattern = Pattern.compile("User102");

        // Create Mock Accounts for Testing

        Account A = Account.createAccount(accountRepository, "User100", "Pass1234!");
        Account B = Account.createAccount(accountRepository, "User101", "Pass1234!");
        Account C = Account.createAccount(accountRepository, "User102", "Pass1234!");

        // Mock Products to add reviews to

        Product example1 = Product.createProduct(productRepository,
                "FountainPen",
                399.99f,
                "A Fountain Pen",
                null,
                null);
        Product example2 = Product.createProduct(productRepository,
                "House",
                10.39f,
                "A House",
                null,
                null);
        Product example3 = Product.createProduct(productRepository,
                "Shed",
                101.39f,
                "A Shed",
                null,
                null);

        // Mock Reviews to be used in Jaccard Distance

        Review rev1 = new Review(A, "Body", Review.Star.THREE);
        Review rev2 = new Review(A, "Body", Review.Star.THREE);
        Review rev3 = new Review(A, "Body", Review.Star.FOUR);

        Review rev4 = new Review(B, "Body", Review.Star.TWO);
        Review rev5 = new Review(B, "Body", Review.Star.THREE);
        Review rev6 = new Review(B, "Body", Review.Star.ONE);

        Review rev7 = new Review(C, "Body", Review.Star.THREE);
        Review rev8 = new Review(C, "Body", Review.Star.THREE);
        Review rev9 = new Review(C, "Body", Review.Star.THREE);

        // Has to be done manually since there is no repo to do this automatically.
        example1.addReview(rev1);
        example1.addReview(rev4);
        example1.addReview(rev7);

        example2.addReview(rev2);
        example2.addReview(rev5);
        example2.addReview(rev8);

        example3.addReview(rev3);
        example3.addReview(rev6);
        example3.addReview(rev9);

        rev1.setProduct(example1);
        rev4.setProduct(example1);
        rev7.setProduct(example1);

        rev2.setProduct(example2);
        rev5.setProduct(example2);
        rev8.setProduct(example2);

        rev3.setProduct(example3);
        rev6.setProduct(example3);
        rev9.setProduct(example3);

        A.addReview(rev1);
        A.addReview(rev2);
        A.addReview(rev3);

        B.addReview(rev4);
        B.addReview(rev5);
        B.addReview(rev6);

        C.addReview(rev7);
        C.addReview(rev8);
        C.addReview(rev9);

        // Mocking Repo for testing

        when(productRepository.findByName("FountainPen")).thenReturn(example1);
        when(productRepository.findByName("House")).thenReturn(example2);
        when(productRepository.findByName("Shed")).thenReturn(example3);

        when(accountRepository.findByUsername("User100")).thenReturn(Optional.of(A));
        when(accountRepository.findByUsername("User101")).thenReturn(Optional.of(B));
        when(accountRepository.findByUsername("User102")).thenReturn(Optional.of(C));

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.get("/product/filter?productName=FountainPen&sort=Jaccard"))
                .andReturn();

        String template = result.getResponse().getContentAsString();

        Matcher matcherUser100 = user100Pattern.matcher(template);
        Matcher matcherUser101 = user101Pattern.matcher(template);
        Matcher matcherUser102 = user102Pattern.matcher(template);

        assertTrue(matcherUser100.find());
        int user100index = matcherUser100.end();

        assertTrue(matcherUser101.find());
        int user101index = matcherUser101.end();

        assertTrue(matcherUser102.find());
        int user102index = matcherUser102.end();

        // Verify that the reviews are sorted correctly
        /*
        User102 is the second closest to User100.
        User101 is the farthest from User100.
        User100 is the closest to User100.
         */
        assertTrue(user100index < user101index && user100index < user102index);
        assertTrue(user102index < user101index);

    }

}
