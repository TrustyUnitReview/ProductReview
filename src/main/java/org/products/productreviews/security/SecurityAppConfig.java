package org.products.productreviews.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Configures security rules for the application
 */
@Configuration
@EnableWebSecurity //enables Spring Security filters to the application's web requests
public class SecurityAppConfig {

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
                        .requestMatchers("/login", "/registration", "/css/registration.css", "/css/login.css", "/logout", "/css/logout.css")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .failureUrl("/login?error=true")
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll()// configures a login form for user authentication
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST")) //need this to allow get requests from other pages
                        .logoutSuccessUrl("/login") //configures logout functionality -> redirects users to /login page on successful output
                        .permitAll()
                );

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
