package org.products.productreviews.unittests.security;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

/**
 * Configuration for security filter chain for testing purposes
 */
@TestConfiguration
public class TestSecurityConfig {

    /**
     * Configures security filter chain to allow all requests to pass through without authentication.
     *
     * @param http HttpSecurity object
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) //allows all requests to pass through without authentication
                .authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());
        return http.build();
    }
}
