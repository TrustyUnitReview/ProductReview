package org.products.productreviews.unittests.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.products.productreviews.ProductReviewsApplication;
import org.products.productreviews.app.entities.Account;
import org.products.productreviews.app.entities.Product;
import org.products.productreviews.app.entities.Review;
import org.products.productreviews.app.repositories.AccountRepository;
import org.products.productreviews.app.repositories.ProductRepository;
import org.products.productreviews.app.repositories.ReviewRepository;
import org.products.productreviews.web.rest.DashboardTemplate;
import org.products.productreviews.web.util.ProductCategory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DashboardTemplateTest {

    @MockBean
    private ProductRepository productRepo;

    @InjectMocks
    private DashboardTemplate dashboardTemplate;

    @MockBean
    private AccountRepository accountRepo;

    @MockBean
    private ReviewRepository reviewRepo;

    private Product product1;
    private Product product2;
    private Product product3;
    private Product product4;
    private Product product5;
    private Product product6;

    private Account account1;
    private Account account2;
    private Account account3;
    private Account account4;
    private Account account5;

    /**
     * Sets up an initial database with products, accounts, and reviews for testing.
     * @throws Exception
     */
    @BeforeEach
    void setUp() throws Exception {
        //define test products
        product1 = Product.createProduct(productRepo, "Reading Device", 199.99f,
                "A tablet used to purchase and read e-books",
                null, ProductCategory.ELECTRONICS);
        product2 = Product.createProduct(productRepo, "Lawnmower2000", 599.99f,
                "A lawnmower to cut grass.",
                null, ProductCategory.LANDSCAPING);
        product3 = Product.createProduct(productRepo, "Book1", 19.99f,
                "A long book, it has many pages.",
                null, ProductCategory.BOOKS);
        product4 = Product.createProduct(productRepo, "Book2", 19.99f,
                "A long book, it has many more pages.",
                null, ProductCategory.BOOKS);
        product5 = Product.createProduct(productRepo, "Pen", 3.99f,
                "This fountain pen is a well-crafted.",
                null, ProductCategory.OFFICE_SUPPLIES);
        product6 = Product.createProduct(productRepo, "IPhone", 1399f,
                "New IPhone.",
                null, ProductCategory.ELECTRONICS);
        productRepo.save(product1);
        productRepo.save(product2);
        productRepo.save(product3);
        productRepo.save(product4);
        productRepo.save(product5);
        List<Product> products = Arrays.asList(product1, product2, product3, product4, product5);

        when(productRepo.findAll()).thenReturn(products);
        when(productRepo.findByCategory(ProductCategory.ELECTRONICS)).thenReturn(Arrays.asList(product1, product6));
        when(productRepo.findByCategory(ProductCategory.LANDSCAPING)).thenReturn(Arrays.asList(product2));
        when(productRepo.findByCategory(ProductCategory.BOOKS)).thenReturn(Arrays.asList(product3, product4));
        when(productRepo.findByCategory(ProductCategory.OFFICE_SUPPLIES)).thenReturn(Arrays.asList(product5));

        //define test accounts
        account1 = Account.createAccount(accountRepo, "user1", "Pass1234!");
        account2 = Account.createAccount(accountRepo, "user2", "Pass1234!");
        account3 = Account.createAccount(accountRepo, "user3", "Pass1234!");
        account4 = Account.createAccount(accountRepo, "user4", "Pass1234!");
        account5 = Account.createAccount(accountRepo, "user5", "Pass1234!");
        accountRepo.save(account1);
        accountRepo.save(account2);
        accountRepo.save(account3);
        accountRepo.save(account4);
        accountRepo.save(account5);

        //define test reviews
        Review r1 = new Review(account1, "Best e-reader out there! I never have to worry about not finding a book I want to read.",
                Review.Star.FOUR);
        account1.addReview(r1);
        Review r2 = new Review(account2, "I really liked this device! However I wish it came in different colours",
                Review.Star.FOUR);
        account2.addReview(r2);
        Review r3 = new Review(account3, "Very nice lawnmower, it has lasted me for years!",
                Review.Star.FIVE);
        account3.addReview(r3);
        Review r4 = new Review(account4, "I've been buying lawnmowers from this company for years. They last a long time and get the job done quickly!",
                Review.Star.FIVE);
        account4.addReview(r4);
        Review r5 = new Review(account1, "Very dry read, I don't understand the hype of Tolstoy.",
                Review.Star.ONE);
        account1.addReview(r5);
        Review r6 = new Review(account5, "Book sounded too scripted, as if the author was rushing. It was a good story but could have been written much better.",
                Review.Star.TWO);
        account5.addReview(r6);
        Review r7 = new Review(account3, "This book was even better than the first! I finished it in a day because of how captivating it was. Highly recommend!",
                Review.Star.FIVE);
        account3.addReview(r7);
        Review r8 = new Review(account2, "Overall this was a good story but do think that the ending could have been better...",
                Review.Star.THREE);
        account2.addReview(r8);
        Review r9 = new Review(account3, "Amazing phone!",
                Review.Star.FIVE);
        account3.addReview(r9);
        Review r10 = new Review(account4, "I love this phone, it's the best one I've ever had!",
                Review.Star.FIVE);
        account4.addReview(r10);

        //assign reviews to products and products to reviews
        product1.addReview(r1);
        r1.setProduct(product1);
        product1.addReview(r2);
        r2.setProduct(product1);

        product2.addReview(r3);
        r3.setProduct(product2);
        product2.addReview(r4);
        r4.setProduct(product2);

        product3.addReview(r7); //product 3 has a true average rating of 4
        r7.setProduct(product3);
        product3.addReview(r8);
        r8.setProduct(product3);

        product4.addReview(r5); //product 4 has a true average rating of 1.5
        r5.setProduct(product4);
        product4.addReview(r6);
        r6.setProduct(product4);

        product6.addReview(r9);
        r9.setProduct(product6);
        product6.addReview(r10);
        r10.setProduct(product6);

        reviewRepo.save(r1);
        reviewRepo.save(r2);
        reviewRepo.save(r3);
        reviewRepo.save(r4);
        reviewRepo.save(r5);
        reviewRepo.save(r6);
        reviewRepo.save(r7);
        reviewRepo.save(r8);
        reviewRepo.save(r9);
        reviewRepo.save(r10);

        account2.addFollows(account1);

    }

    /**
     * Deletes all products from the database after each test.
     */
    @AfterEach
    void tearDown(){
        productRepo.deleteAll();
    }

    /**
     * Tests sorting products by a valid category.
     */
    @Test
    void testSortProductsByValidCategory() {
        ProductCategory validCategory = ProductCategory.ELECTRONICS;
        List<Product> sortedProducts = productRepo.findByCategory(validCategory); //stub for sortProductsByCategory
        assertEquals(2, sortedProducts.size());
    }

    /**
     * Tests sorting products by an invalid category.
     */
    @Test
    void testSortProductsByInValidCategory() {
        assertThrows(IllegalArgumentException.class, () -> {
            ProductCategory invalidCategory = ProductCategory.fromDisplayName("INVALID");
            productRepo.findByCategory(invalidCategory); //stub for sortProductsByCategory
        });

    }

    /**
     * Tests sorting a product by average rating when there are multiple reviews on product
     * Details:
     *  product3 and product4 are books
     *  product3 has two ratings: 3 and 5, average is 4
     *  product4 has two ratings: 1 and 3, average is 1.5
     *  product4 should be first in the list, followed by product3
     */
    @Test
    void testSortProductsAvgMultipleRatings(){

        List<Product> bookProducts = productRepo.findByCategory(ProductCategory.BOOKS);
        List<Product> sortedProducts = dashboardTemplate.sortProductsAvgRating(bookProducts);
        assertEquals(2, sortedProducts.size());
        assertEquals(product3, sortedProducts.get(0));
        assertEquals(product4, sortedProducts.get(1));

    }

    /**
     * Tests sorting a product by average rating when there is only one review on product
     */
    @Test
    void testSortProductsAvgSingleRatings(){
        //product1 is the only landscaping product
        List<Product> landscapingProducts = productRepo.findByCategory(ProductCategory.LANDSCAPING);
        List<Product> sortedProducts = dashboardTemplate.sortProductsAvgRating(landscapingProducts);
        assertEquals(1, sortedProducts.size());
        assertEquals(product2, sortedProducts.get(0));
    }

    /**
     * Tests sorting a product by average rating, only considering ratings by reviews left by followed users.
     * Have to stub the method that calculates the average rating of a product based on reviews left by followed users.
     * Details:
     *   User2 follows User1
     *   User1 has left a review for product1 (4 stars), an electronic product
     *   User1 has not left any review for product6 which is also an electronic product
     *   Product1 has a true average rating of 4 stars
     *   Product6 has a true average rating of 5 stars
     *   Since user1 has only left a review on Product1 and not Product6,
     *   the average rating of Product1 is higher, even though the true average rating of
     *   Product6 is higher
     *
     */
    @Test
    void testSortProductsAvgRatingUsersFollowing1(){

        account2.addFollows(account1);
        List<Product> expectedSortedProducts = Arrays.asList(product1, product6);
        List<Product> electronicProducts = productRepo.findByCategory(ProductCategory.ELECTRONICS);
        List<Product> sortedProducts = sortProductsAvgRatingUsersFollowingStub(electronicProducts, account2);
        assertEquals(2, sortedProducts.size());
        assertEquals(expectedSortedProducts, sortedProducts);
    }

    /**
     * Tests sorting a product by average rating, only considering ratings by reviews left by followed users.
     * Have to stub the method that calculates the average rating of a product based on reviews left by followed users.
     * Details:
     *   User2 follows User1
     *   User1 has left a review for product4 (1 star), a book
     *   User1 has not left any review for product3 which is also a book
     *   Product3 has a true average rating of 4 stars
     *   Product4 has a true average rating of 1.5 stars
     *   Since user1 has only left a review on Product4 and not Product3, the average rating of Product4
     *   is higher, even though the true average rating of Product3 is higher
     */
    @Test
    void testSortProductsAvgRatingUsersFollowing2(){
        account2.addFollows(account1);
        List<Product> expectedSortedProducts2 = Arrays.asList(product4, product3);
        List<Product> bookProducts = productRepo.findByCategory(ProductCategory.BOOKS);
        List<Product> sortedProducts2 = sortProductsAvgRatingUsersFollowingStub(bookProducts, account2);
        assertEquals(2, sortedProducts2.size());
        assertEquals(expectedSortedProducts2, sortedProducts2);
        assertEquals(product4.getName(), sortedProducts2.get(0).getName());

    }

    /**
     * Tests calculating the average rating of a product based on reviews left by followed users when there are no reviews.
     */
    @Test
    void testCalculateAverageRatingFromFollowedUsers_NoReviews() {
        Set<Account> followedUsers = Set.of(account1, account2);
        float avgRating = dashboardTemplate.calculateAverageRatingFromFollowedUsers(product5, followedUsers);
        assertEquals(0.0f, avgRating);
    }

    /**
     * Tests calculating the average rating of a product based on reviews left by followed users when there is one review.
     */
    @Test
    void testCalculateAverageRatingFromFollowedUsers_SingleReview() {
        Set<Account> followedUsers = Set.of(account1);
        float avgRating = dashboardTemplate.calculateAverageRatingFromFollowedUsers(product1, followedUsers);
        assertEquals(4.0f, avgRating);
    }

    /**
     * Tests calculating the average rating of a product based on reviews left by followed users when there are multiple reviews.
     */
    @Test
    void testCalculateAverageRatingFromFollowedUsers_MultipleReviews() {
        Set<Account> followedUsers = Set.of(account1, account2);
        float avgRating = dashboardTemplate.calculateAverageRatingFromFollowedUsers(product1, followedUsers);
        assertEquals(4.0f, avgRating);
    }

    /**
     * Stub method to sort products by average rating based on reviews left by followed users.
     * @param products List of products to sort
     * @param currUser The user whose followed users' reviews will be considered
     * @return List of products sorted by average rating based on reviews left by followed users
     */
    private List<Product> sortProductsAvgRatingUsersFollowingStub(List<Product> products, Account currUser){
        //get people user follows
        Set<Account> followedUsers = currUser.getFollows();
        ProductReviewsApplication.LOGGER.log(Level.INFO, "Followed Users:");
        for(Account user : followedUsers){
            ProductReviewsApplication.LOGGER.log(Level.INFO, user.getUsername());
        }

        //calculate average rating for each product based on ratings from followed users
        //sort products based on calculated average ratings
        products.sort((p1, p2) -> {
            double avgRatingP1 = dashboardTemplate.calculateAverageRatingFromFollowedUsers(p1, followedUsers); //assuming these are right, we are testing them below
            double avgRatingP2 = dashboardTemplate.calculateAverageRatingFromFollowedUsers(p2, followedUsers);
            return Double.compare(avgRatingP2, avgRatingP1); // Descending order
        });

        return products;

    }


}
