package org.products.productreviews.unittests.web;

import org.junit.jupiter.api.Test;
import org.products.productreviews.app.entities.Account;
import org.products.productreviews.app.repositories.AccountRepository;
import org.products.productreviews.web.rest.AccountAPI;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for @AccountAPI controller
 */
@WebMvcTest(AccountAPI.class)
public class AccountAPITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountRepository accountRepository;

//    @Test
//    void testFollowAccount() throws Exception {
//        Account account = Account.createAccount(accountRepository, "user", "test");
//        Account otherAccount = Account.createAccount(accountRepository, "test", "test");
//
//        when(accountRepository.findByUsername("user")).thenReturn(account);
//        when(accountRepository.findByUsername("test")).thenReturn(otherAccount);
//
//        mockMvc.perform(post("/account/test/follow").with(user("user").password("test").roles("ADMIN")))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void testUnfollowAccount() throws Exception {
//        Account account = Account.createAccount(accountRepository, "user", "test");
//        Account otherAccount = Account.createAccount(accountRepository, "test", "test");
//
//        when(accountRepository.findByUsername("user")).thenReturn(account);
//        when(accountRepository.findByUsername("test")).thenReturn(otherAccount);
//
//        mockMvc.perform(post("/account/test/unfollow").with(user("user").password("test").roles("ADMIN")))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
}
