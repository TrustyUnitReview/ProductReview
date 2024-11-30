package org.products.productreviews.unittests.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.products.productreviews.app.entities.Account;
import org.products.productreviews.app.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountRepository accountRepo;

    /**
     * Tests that the registration page is accessible
     * @throws Exception if an error occurs during the request
     */
    @Test
    public void testRegisterGet() throws Exception {
        mockMvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("accountRegistration"));
    }

    /**
     * Tests a successful registration
     * @throws Exception if an error occurs during the request
     */
    @Test
    public void testRegisterPostSuccess() throws Exception {
        Account account = Account.createAccount(accountRepo, "testuser", "Password1!");
        accountRepo.save(account);

        when(accountRepo.existsByUsername("testuser")).thenReturn(false);

        mockMvc.perform(post("/registration").with(csrf())
                        .param("username", "testuser")
                        .param("password", "Password1!"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login")); //successful registration redirects to login page

    }

    /**
     * Tests a registration with an existing username.
     * The registration page should be displayed again with an error message
     * @throws Exception if an error occurs during the request
     */
    @Test
    public void testRegisterPostExistingUsername() throws Exception {
        when(accountRepo.existsByUsername("testuser")).thenReturn(true);

        mockMvc.perform(post("/registration").with(csrf())
                        .param("username", "testuser")
                        .param("password", "Password1!"))
                .andExpect(status().isOk())
                .andExpect(view().name("accountRegistration"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Username is already in use"));
    }

    /**
     * Tests a registration with an invalid username.
     * The registration page should be displayed again with an error message
     * @throws Exception if an error occurs during the request
     */
    @Test
    public void testRegisterPostInvalidUsername() throws Exception {
        mockMvc.perform(post("/registration").with(csrf())
                        .param("username", "te")
                        .param("password", "Password1!"))
                .andExpect(status().isOk())
                .andExpect(view().name("accountRegistration"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Username must be between 3 and 20 characters long"));

    }

    /**
     * Tests a registration with an invalid password.
     * The registration page should be displayed again with an error message
     * @throws Exception if an error occurs during the request
     */
    @Test
    public void testRegisterPostInvalidPassword() throws Exception {
        mockMvc.perform(post("/registration").with(csrf())
                        .param("username", "testuser")
                        .param("password", "pass"))
                .andExpect(status().isOk())
                .andExpect(view().name("accountRegistration"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Password must be between 8 and 20 characters long"));

    }

    /**
     * Tests a post request to /registration endpoint without csrf token.
     * The request should be forbidden due to csrf protection
     * @throws Exception if an error occurs during the request
     */
    @Test
    public void testRegisterPostWithoutCsrf() throws Exception {
        mockMvc.perform(post("/registration")
                        .param("username", "testuser")
                        .param("password", "Password1!"))
                .andExpect(status().isForbidden());
    }

}
