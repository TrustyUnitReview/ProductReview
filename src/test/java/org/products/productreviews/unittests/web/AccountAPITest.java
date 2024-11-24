package org.products.productreviews.unittests.web;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.products.productreviews.app.entities.Account;
import org.products.productreviews.app.repositories.AccountRepository;
import org.products.productreviews.app.repositories.ProductRepository;
import org.products.productreviews.web.rest.AccountAPI;
import org.products.productreviews.web.rest.DashboardTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for @AccountAPI controller
 */
@WebMvcTest(AccountAPI.class)
@Import(DashboardTemplate.class)
public class AccountAPITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean ProductRepository productRepository;

    private Account account, otherAccount;

    @BeforeEach
    void setup() throws InvalidFormatException {
        account = Account.createAccount(accountRepository, "user", "Password1!");
        otherAccount = Account.createAccount(accountRepository, "test", "Password1!");
        when(accountRepository.findByUsername("user")).thenReturn(Optional.of(account));
        when(accountRepository.findByUsername("test")).thenReturn(Optional.of(otherAccount));
    }

    @Test
    @WithMockUser(username = "user", password = "Password1!")
    void testFollowAccount() throws Exception {
        assertTrue(account.getFollows().isEmpty());
        mockMvc.perform(post("/account/test/follow").with(csrf()))
                .andExpect(status().isOk());

        //confirms that account was followed
        assertFalse(account.getFollows().isEmpty());
        assertTrue(account.getFollows().contains(otherAccount));
    }

    @Test
    @WithMockUser(username = "user", password = "Password1!")
    void testUnfollowAccount() throws Exception {

        mockMvc.perform(post("/account/test/follow").with(csrf()))
                .andExpect(status().isOk());

        assertFalse(account.getFollows().isEmpty());

        mockMvc.perform(post("/account/test/unfollow").with(csrf()))
                .andExpect(status().isOk());

        //confirms that account was unfollowed
        assertTrue(account.getFollows().isEmpty());
    }
}
