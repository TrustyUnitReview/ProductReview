package org.products.productreviews.unittests.app.entities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.products.productreviews.app.entities.Account;
import org.products.productreviews.app.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.management.openmbean.InvalidKeyException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Account entity
 */
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
        Account validAccount = Account.createAccount(repo,"testUser1", "MyPassword1!");
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
    void createUserValid() {
        // Test valid user creation cases
        assertDoesNotThrow(() -> Account.createAccount(repo, "newUser", "MyPassword1!"));
    }

    /**
     * We want to test an invalid username (one which already exists).
     * If the correct exception is thrown (InvalidKeyException), test passes.
     * Other exceptions fail it.
     */
    @Test
    void createUserRepeatUsername() {
        // Checks the expected exception is thrown
        assertThrows(InvalidKeyException.class, () -> Account.createAccount(repo, "testUser1", "MyPassword2!"));

    }

    /**
     * Tests a valid username during account creation
     */
    @Test
    void checkUsernameFormat_Valid() {
        assertDoesNotThrow(() -> Account.createAccount(repo, "validUser", "ValidPass1!"));
    }

    /**
     * Tests an invalid username with an invalid length
     */
    @Test
    void checkUsernameFormat_InvalidLength() {
        assertThrows(IllegalArgumentException.class, () -> Account.createAccount(repo, "ab", "ValidPass1!"));
        assertThrows(IllegalArgumentException.class, () -> Account.createAccount(repo, "a".repeat(21), "ValidPass1!"));
    }

    /**
     * Tests an invalid username with invalid characters
     */
    @Test
    void checkUsernameFormat_InvalidCharacters() {
        assertThrows(IllegalArgumentException.class, () -> Account.createAccount(repo, "invalid user", "ValidPass1!"));
        assertThrows(IllegalArgumentException.class, () -> Account.createAccount(repo, "invalid@user", "ValidPass1!"));
    }

    /**
     * Tests a valid password during account creation
     */
    @Test
    void checkPasswordFormat_Valid() {
        assertDoesNotThrow(() -> Account.createAccount(repo, "validUser", "ValidPass1!"));
    }

    /**
     * Tests an invalid password with an invalid length
     */
    @Test
    void checkPasswordFormat_InvalidLength() {
        assertThrows(IllegalArgumentException.class, () -> Account.createAccount(repo, "validUser", "Short1!"));
        assertThrows(IllegalArgumentException.class, () -> Account.createAccount(repo, "validUser", "a".repeat(21) + "1!"));
    }

    /**
     * Tests an invalid password with missing characters
     */
    @Test
    void checkPasswordFormat_MissingDigit() {
        assertThrows(IllegalArgumentException.class, () -> Account.createAccount(repo, "validUser", "NoDigitPass!"));
    }

    /**
     * Tests an invalid password with missing characters
     */
    @Test
    void checkPasswordFormat_MissingLowercase() {
        assertThrows(IllegalArgumentException.class, () -> Account.createAccount(repo, "validUser", "NOLOWERCASE1!"));
    }

    /**
     * Tests an invalid password with missing characters
     */
    @Test
    void checkPasswordFormat_MissingUppercase() {
        assertThrows(IllegalArgumentException.class, () -> Account.createAccount(repo, "validUser", "nouppercase1!"));
    }

    /**
     * Tests an invalid password with missing characters
     */
    @Test
    void checkPasswordFormat_MissingSpecialCharacter() {
        assertThrows(IllegalArgumentException.class, () -> Account.createAccount(repo, "validUser", "NoSpecialChar1"));
    }


}