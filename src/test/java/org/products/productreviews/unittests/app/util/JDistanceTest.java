package org.products.productreviews.unittests.app.util;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.products.productreviews.app.entities.Account;
import org.products.productreviews.app.entities.Product;
import org.products.productreviews.app.entities.Review;
import org.products.productreviews.app.repositories.AccountRepository;
import org.products.productreviews.app.repositories.ProductRepository;
import org.products.productreviews.app.util.JDistance;

import java.security.InvalidKeyException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Tests Jaccard distance
 */
public class JDistanceTest {

    @Mock
    AccountRepository accountRepository;

    @Mock
    ProductRepository productRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(accountRepository.existsByUsername("User100")).thenReturn(Boolean.FALSE);
        when(accountRepository.existsByUsername("User101")).thenReturn(Boolean.FALSE);
    }

    /**
     * Tests Jaccard distance if both accounts have no reviews
     */
    @Test
    public void testJDistanceBothNoReviews() throws InvalidFormatException {
        Account A = Account.createAccount(accountRepository, "User100", "Pass1234!");
        Account B = Account.createAccount(accountRepository, "User101", "Pass1234!");

        JDistance jDistanceAB = new JDistance(A, B);
        float result = jDistanceAB.distance();
        assertEquals(0, result);

    }

    /**
     * Tests Jaccard distance if one account has no reviews
     */
    @Test
    public void testJDistanceOneNoReviews() throws InvalidFormatException, InvalidKeyException {
        Account A = Account.createAccount(accountRepository, "User100", "Pass1234!");
        Account B = Account.createAccount(accountRepository, "User101", "Pass1234!");

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

        Review rev1 = new Review(A, "Body", Review.Star.THREE);
        Review rev2 = new Review(A, "Body", Review.Star.THREE);

        rev1.setProduct(example1);
        rev2.setProduct(example2);

        A.addReview(rev1);
        A.addReview(rev2);

        JDistance jDistanceAB = new JDistance(A, B);
        float result = jDistanceAB.distance();
        assertEquals(0, result);

    }

    /**
     * Tests Jaccard distance if both accounts have reviews, some matching some that are not.
     */
    @Test
    public void testJDistanceBothReviews() throws InvalidFormatException, InvalidKeyException {
        Account A = Account.createAccount(accountRepository, "User100", "Pass1234!");
        Account B = Account.createAccount(accountRepository, "User101", "Pass1234!");

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

        Review rev1 = new Review(A, "Body", Review.Star.THREE);
        Review rev2 = new Review(A, "Body", Review.Star.THREE);

        Review rev3 = new Review(B, "Body", Review.Star.TWO);
        Review rev4 = new Review(B, "Body", Review.Star.THREE);

        rev1.setProduct(example1);
        rev3.setProduct(example1);

        rev2.setProduct(example2);
        rev4.setProduct(example2);

        A.addReview(rev1);
        A.addReview(rev2);

        B.addReview(rev3);
        B.addReview(rev4);

        JDistance jDistanceAB = new JDistance(A, B);
        float result = jDistanceAB.distance();
        assertEquals(1f/3f, result);

    }

    /**
     * Makes sure that JDistance will rank users that are closer higher. The closer to 1 the distance is the more
     * similar they are.
     */
    @Test
    public void testJDistanceIsClosest() throws InvalidFormatException, InvalidKeyException {
        Account A = Account.createAccount(accountRepository, "User100", "Pass1234!");
        Account B = Account.createAccount(accountRepository, "User101", "Pass1234!");
        Account C = Account.createAccount(accountRepository, "User102", "Pass1234!");

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

        Review rev1 = new Review(A, "Body", Review.Star.THREE);
        Review rev2 = new Review(A, "Body", Review.Star.THREE);

        Review rev3 = new Review(B, "Body", Review.Star.TWO);
        Review rev4 = new Review(B, "Body", Review.Star.THREE);

        Review rev5 = new Review(C, "Body", Review.Star.THREE);
        Review rev6 = new Review(C, "Body", Review.Star.THREE);

        rev1.setProduct(example1);
        rev3.setProduct(example1);
        rev5.setProduct(example1);

        rev2.setProduct(example2);
        rev4.setProduct(example2);
        rev6.setProduct(example2);

        A.addReview(rev1);
        A.addReview(rev2);

        B.addReview(rev3);
        B.addReview(rev4);

        C.addReview(rev5);
        C.addReview(rev6);

        JDistance jDistance = new JDistance(A, B);

        float result_AB = jDistance.distance();

        jDistance.setAccountB(C);
        float result_AC = jDistance.distance();

        assertTrue(result_AC > result_AB, "Account that should be closer was not closer");
    }

}
