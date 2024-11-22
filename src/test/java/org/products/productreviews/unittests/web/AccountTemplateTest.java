package org.products.productreviews.unittests.web;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.jupiter.api.Test;
import org.products.productreviews.app.entities.Account;
import org.products.productreviews.app.entities.Product;
import org.products.productreviews.app.entities.Review;
import org.products.productreviews.app.repositories.AccountRepository;
import org.products.productreviews.web.rest.AccountAPI;
import org.products.productreviews.web.rest.AccountTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.security.InvalidKeyException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountTemplate.class)
public class AccountTemplateTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AccountRepository accountRepository;

    /**
     * Tests the template that is returned when requesting reviews for a product name
     * Looks for specific HTML elements
     */
    @Test
    void testShowOtherAccount() throws Exception {
        Account account = Account.createAccount(accountRepository, "user", "user");
        Account otherAccount = Account.createAccount(accountRepository, "test", "test");

        when(accountRepository.findByUsername("user")).thenReturn(account);
        when(accountRepository.findByUsername("test")).thenReturn(otherAccount);

        mockMvc.perform(get("/account/test"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("otherAccount"))
                .andExpect(model().attribute("account", account))
                .andExpect(model().attribute("otherAccount", otherAccount));


    }

}
