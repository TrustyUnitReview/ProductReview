package org.products.productreviews.unittests.app.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.products.productreviews.app.entities.Account;
import org.products.productreviews.app.repositories.AccountRepository;
import org.products.productreviews.app.services.AccountService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the AccountService class
 */
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepo;

    @InjectMocks // Injects the mocked AccountRepository into the AccountService
    private AccountService accountService;

    /**
     * Initializes mock objects
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the loadUserByUsername method with a valid username
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testLoadUserByUsername_Success() throws Exception{
        Account account = Account.createAccount(accountRepo, "testuser", "MyPassword1!");

        when(accountRepo.findByUsername("testuser")).thenReturn(Optional.of(account));

        UserDetails userDetails = accountService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals(account.getPassword(), userDetails.getPassword());
    }

    /**
     * Tests the loadUserByUsername method with a non-existent username
     */
    @Test
    public void testLoadUserByUsername_UserNotFound() {
        when(accountRepo.findByUsername("nonexistentuser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> accountService.loadUserByUsername("nonexistentuser"));
    }
}
