package org.products.productreviews.unittests.app.entities;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.jupiter.api.Test;
import org.products.productreviews.app.entities.User;
import org.products.productreviews.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.management.openmbean.InvalidKeyException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserTest {
    @Autowired
    private UserRepository repo;

    /**
     * We want to test a valid username, password combination.
     * If an exception is thrown, test will fail!
     */
    @Test
    void createUserValid() throws Exception {
        // Test valid user creation cases
        User validUser = User.createUser(repo, "newUser", "pass1");
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
        {User.createUser(repo, "testUser1", "pass2");}
        );
    }

    // Note: No tests for invalidFormats because those methods are not defined yet.
}