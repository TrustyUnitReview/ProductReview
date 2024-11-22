package org.products.productreviews.security;

import org.products.productreviews.app.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configures security rules for the application
 */
@Configuration
@EnableWebSecurity //enables Spring Security filters to the application's web requests
public class SecurityAppConfig {
    @Autowired
    private AccountService userDetailsService;

    /**
     * Configures security rules for HTTP requests,
     * defines how requests are authorized,
     * and sets up form-based login and logout behavior
     * @param http HttpSecurity object
     * @return SecurityFilterChain object
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests //URL authorization
                        .requestMatchers("/login", "/registration", "/registration-form").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll()// configures a login form for user authentication
                )
                .logout(logout -> logout.logoutSuccessUrl("/login")); //configures logout functionality -> redirects users to /login page on successful output

        return http.build();
    }

    /**
     * Provides a password encoder for the application,
     * uses BCrypt hashing function to encode passwords
     * @return PasswordEncoder object
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
