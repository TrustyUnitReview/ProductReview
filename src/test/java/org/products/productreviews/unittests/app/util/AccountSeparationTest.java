package org.products.productreviews.unittests.app.util;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.products.productreviews.app.entities.Account;
import org.products.productreviews.app.repositories.AccountRepository;
import org.products.productreviews.app.util.AccountSeparation;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the AccountSeparation algorithm
 */
public class AccountSeparationTest {

    @Mock
    AccountRepository accountRepository;

    private Account activeAccount, directAccount, secondLinkAccount, thirdLinkAccount, fourthLinkAccount, goalAccount;

    @BeforeEach
    void setup() throws InvalidFormatException {
        MockitoAnnotations.openMocks(this);

        activeAccount = Account.createAccount(accountRepository, "active", "Password1!");
        directAccount = Account.createAccount(accountRepository, "friend", "Password1!");
        secondLinkAccount = Account.createAccount(accountRepository, "friendOfFriend", "Password1!");
        thirdLinkAccount = Account.createAccount(accountRepository, "friendCubed", "Password1!");
        fourthLinkAccount = Account.createAccount(accountRepository, "4dFriend", "Password1!");
        goalAccount = Account.createAccount(accountRepository, "Friend", "Password1!");
    }

    /**
     * Testing two accounts which have no link
     */
    @Test
    public void testNoSeparation() {
        assertEquals(-1, AccountSeparation.getSeparation(activeAccount, goalAccount));
    }

    /**
     * Testing with the same account
     */
    @Test
    public void testSameAccountSeparation() {
        assertEquals(-1, AccountSeparation.getSeparation(activeAccount, activeAccount));
    }

    /**
     * Testing an account which is directly followed
     */
    @Test
    public void testDirectFollower() {
        activeAccount.addFollows(goalAccount);

        assertEquals(0, AccountSeparation.getSeparation(activeAccount, goalAccount));
    }

    /**
     * Testing for account which has 1 degree of separation
     */
    @Test
    public void testOneDegreeOfSeparation() {
        activeAccount.addFollows(directAccount);
        directAccount.addFollows(goalAccount);

        assertEquals(1, AccountSeparation.getSeparation(activeAccount, goalAccount));
    }

    /**
     * Testing for an account which 2 degrees of separation
     */
    @Test
    public void testTwoDegreesOfSeparation() {
        activeAccount.addFollows(directAccount);
        directAccount.addFollows(secondLinkAccount);
        secondLinkAccount.addFollows(goalAccount);

        assertEquals(2, AccountSeparation.getSeparation(activeAccount, goalAccount));
    }

    /**
     * Testing for an account which has three degrees of separation
     */
    @Test
    public void testThreeDegreesOfSeparation() {
        activeAccount.addFollows(directAccount);
        directAccount.addFollows(secondLinkAccount);
        secondLinkAccount.addFollows(thirdLinkAccount);
        thirdLinkAccount.addFollows(goalAccount);

        assertEquals(3, AccountSeparation.getSeparation(activeAccount, goalAccount));
    }

    /**
     * Testing for an account which has greater than three degrees of separation
     */
    @Test
    public void testGreaterDegreesOfSeparation() {
        activeAccount.addFollows(directAccount);
        directAccount.addFollows(secondLinkAccount);
        secondLinkAccount.addFollows(thirdLinkAccount);
        thirdLinkAccount.addFollows(fourthLinkAccount);
        fourthLinkAccount.addFollows(goalAccount);

        assertEquals(3, AccountSeparation.getSeparation(activeAccount, goalAccount));
    }

    /**
     * Testing for a follow link which has a cycle in the graph
     */
    @Test
    public void testCyclicFollowing() {
        activeAccount.addFollows(directAccount);
        directAccount.addFollows(secondLinkAccount);
        secondLinkAccount.addFollows(directAccount); // Ensuring that algorithm doesn't get stuck in a loop
        secondLinkAccount.addFollows(thirdLinkAccount);
        thirdLinkAccount.addFollows(goalAccount);

        assertEquals(3, AccountSeparation.getSeparation(activeAccount, goalAccount));
    }

    /**
     * Testing for the shortest path taken.
     */
    @Test
    public void testShortestPath() {
        // Separation of 3 degrees
        activeAccount.addFollows(directAccount);
        directAccount.addFollows(secondLinkAccount);
        secondLinkAccount.addFollows(thirdLinkAccount);
        thirdLinkAccount.addFollows(goalAccount);

        //Separation of 2 degrees
        directAccount.addFollows(fourthLinkAccount);
        fourthLinkAccount.addFollows(goalAccount);

        assertEquals(2, AccountSeparation.getSeparation(activeAccount, goalAccount));
    }
}
