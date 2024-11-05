package org.products.productreviews.unittests.app.repositories;

import org.junit.jupiter.api.Test;
import org.products.productreviews.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {
    @Autowired
    private UserRepository repo;

    /**
     * This test checks if the existsByUsername works as intended.
     * That is, returns true if the username exists in the DB, and false otherwise.
     */
    @Test
    void existsByUsername() {
        // TODO: Figure out how to select for this test a DB which contains testUser1 (and not userNotInDB).
        assertTrue(repo.existsByUsername("testUser1"));
        assertFalse(repo.existsByUsername("userNotInDB"));
    }
}