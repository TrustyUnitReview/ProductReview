package org.products.productreviews.unittests.app.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.products.productreviews.app.entities.Account;
import org.products.productreviews.app.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountRepositoryTest {
    @Autowired
    private AccountRepository repo;

    /**
     * Setup expected database for following tests.
     * @throws Exception if the factory creation fails (it shouldn't in theory)
     */
    @BeforeEach
    void setUp() throws Exception {
        Account validAccount = Account.createAccount(repo,"testUser1", "p1");
        repo.save(validAccount);
    }

    /**
     * Drop table to set it up again.
     */
    @AfterEach
    void tearDown(){
        repo.deleteAll();
    }

    /**
     * This test checks if the existsByUsername works as intended.
     * That is, returns true if the username exists in the DB, and false otherwise.
     */
    @Test
    void existsByUsername() {
        assertTrue(repo.existsByUsername("testUser1"));
        assertFalse(repo.existsByUsername("userNotInDB"));
    }
}