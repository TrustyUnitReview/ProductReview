package org.products.productreviews.unittests.app.entities;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.products.productreviews.app.entities.Account;
import org.products.productreviews.app.entities.Product;
import org.products.productreviews.app.entities.Review;
import org.products.productreviews.app.repositories.AccountRepository;
import org.products.productreviews.app.repositories.ProductRepository;
import org.products.productreviews.web.util.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.InvalidKeyException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductTest {
    @Autowired
    private ProductRepository repo;

    @Autowired
    private AccountRepository accountRepo;

    private Account testAccount;

    /**
     * Setup expected database for following tests.
     * @throws Exception if the factory creation fails (it shouldn't in theory)
     */
    @BeforeEach
    void setUp() throws Exception {
        Product validProduct = Product.createProduct(repo,"prodName1", 100f,"desc1", "img.png", ProductCategory.OFFICE_SUPPLIES);
        repo.save(validProduct);
        testAccount = Account.createAccount(accountRepo, "testProdUser", "MyPassword1!");
        accountRepo.save(testAccount);
    }

    /**
     * Drop table to set it up again.
     */
    @AfterEach
    void tearDown(){
        repo.deleteAll();
        accountRepo.deleteAll();
    }

    /**
     * We want to test a valid product, no exception is thrown.
     *
     * If an exception is thrown, test will fail!
     */
    @Test
    void createProductValid() throws Exception {
        // Test valid user creation cases
        Product valProduct = Product.createProduct(repo, "newProd", 100f, "desc2", "img.png", ProductCategory.OFFICE_SUPPLIES);
    }

    /**
     * We want to test an invalid product name (one which already exists).
     * If the correct exception is thrown (InvalidKeyException), test passes.
     * Other exceptions fail it.
     */
    @Test
    void createProductRepeatName(){
        // Checks the expected exception is thrown
        assertThrows(InvalidKeyException.class, () ->
                Product.createProduct(repo, "prodName1", 100f, "_", "_", ProductCategory.OFFICE_SUPPLIES)
        );
    }

    /**
     * We want to test the scoring system of reviews for a product.
     *
     * Currently, stubbed! Not much to do yet.
     */
    @Test
    void productScore() throws InvalidFormatException{
        Product testProduct = repo.findByName("prodName1");
        // Stubbing test value set to 0.0f for now
        assertEquals(0.0f, testProduct.getAvgReviewScore());
    }

    //following tests are for the getAvgReviewScore method

    /**
     * Tests when a product has no reviews.
     */
    @Test
    void testGetAvgReviewScoreNoReviews() {
        Product testProduct = repo.findByName("prodName1");
        assertEquals(0.0f, testProduct.getAvgReviewScore());
    }

    /**
     * Tests when a product has one review so average will be value of the one review.
     */
    @Test
    void testGetAvgReviewScoreOneReview(){
        Product testProduct = repo.findByName("prodName1");
        Review r1 = new Review(testAccount, "Body", Review.Star.ONE);
        testProduct.addReview(r1);
        assertEquals(1.0f, testProduct.getAvgReviewScore());
    }

    /**
     * Tests when a product has multiple reviews so average will be the average of all reviews.
     */
    @Test
    void testGetAvgReviewScoreMultipleReviews(){
        Product testProduct = repo.findByName("prodName1");
        Review r1 = new Review(testAccount, "Body", Review.Star.ONE);
        Review r2 = new Review(testAccount, "Body", Review.Star.TWO);
        Review r3 = new Review(testAccount, "Body", Review.Star.THREE);
        testProduct.addReview(r1);
        testProduct.addReview(r2);
        testProduct.addReview(r3);
        assertEquals(2.0f, testProduct.getAvgReviewScore());
    }

    /**
     * Tests the getRoundedAvgReviewScore method for a product with multiple reviews, ensuring the average review score
     * is correctly rounded to one decimal place.
     */
    @Test
    void testGetRoundedAvgReviewScore(){
        Product testProduct = repo.findByName("prodName1");
        Review r1 = new Review(testAccount, "Body1", Review.Star.ONE);
        Review r2 = new Review(testAccount, "Body2", Review.Star.THREE);
        Review r3 = new Review(testAccount, "Body3", Review.Star.ONE);
        testProduct.addReview(r1);
        testProduct.addReview(r2);
        testProduct.addReview(r3);
        assertEquals(1.7, testProduct.getRoundedAvgReviewScore());
    }
}