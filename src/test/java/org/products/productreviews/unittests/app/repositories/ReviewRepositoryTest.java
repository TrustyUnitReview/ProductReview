package org.products.productreviews.unittests.app.repositories;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.products.productreviews.app.entities.Account;
import org.products.productreviews.app.entities.Review;
import org.products.productreviews.app.repositories.AccountRepository;
import org.products.productreviews.app.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private AccountRepository accountRepository;


    @BeforeEach
    void setUpBeforeClass() throws InvalidFormatException {
        Account account = Account.createAccount(accountRepository, "uName", "pWord");
        Review review = new Review(account, "Body", Review.Star.THREE);
        accountRepository.save(account);
        reviewRepository.save(review);
    }

    @AfterEach
    void tearDownAfterClass() {
        accountRepository.deleteAll();
        reviewRepository.deleteAll();
    }

    /**
     * Tests retrieving Reviews by ID
     */
    @Test
    void testGetByID(){
        Review review = reviewRepository.findById(1L).orElseThrow();
        Account account = review.getAccount();
        assertEquals("Body", review.getBody());
        assertEquals("uName", account.getUsername());
    }

}
