package org.products.productreviews.unittests.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests the security configuration of the application
 */
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityAppConfigTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Tests that the public endpoints are accessible without authentication
     * @throws Exception if an error occurs during the request
     */
    @Test
    public void testPublicEndpoints() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/registration"))
                .andExpect(status().isOk());
    }

    /**
     * Tests that the authenticated endpoints are accessible with authentication
     * @throws Exception if an error occurs during the request
     */
    @Test
    @WithMockUser //sets up a mock authenticated user
    public void testAuthenticatedEndpoints() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk());
    }

    /**
     * Tests that the authenticated endpoints are not accessible without authentication
     * @throws Exception if an error occurs during the request
     */
    @Test
    public void testAuthenticatedEndpointsWithoutUser() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().is3xxRedirection());
    }

    /**
     * Tests that the application redirects to the login page when not authenticated
     * @throws Exception if an error occurs during the request
     */
    @Test
    public void testRedirectToLoginWhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }
}
