package org.products.productreviews.unittests.app.entities;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.products.productreviews.app.entities.Account;
import org.products.productreviews.app.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.management.openmbean.InvalidKeyException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountTest {
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
     * We want to test a valid username, password combination.
     * If an exception is thrown, test will fail!
     */
    @Test
    void createUserValid() throws Exception {
        // Test valid user creation cases
        Account validAccount = Account.createAccount(repo, "newUser", "pass1");
    }

    /**
     * We want to test an invalid username (one which already exists).
     * If the correct exception is thrown (InvalidKeyException), test passes.
     * Other exceptions fail it.
     */
    @Test
    void createUserRepeatUsername() throws InvalidFormatException{
        // TODO: Figure out how to select for this test a DB which contains testUser1
        // Checks the expected exception is thrown
        assertThrows(InvalidKeyException.class, () ->
        {
            Account.createAccount(repo, "testUser1", "pass2");}
        );
    }

    // Note: No tests for invalidFormats because those methods are not defined yet.
}