package org.products.productreviews.unittests.app.repositories;

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

    @BeforeEach
    void setUp() throws Exception {
        // TODO: Figure out how to select for this test a DB which contains testUser1 (and not userNotInDB).
        Account validAccount = Account.createUser(repo,"testUser1", "p1");
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