package org.products.productreviews.security;

import org.products.productreviews.app.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity //enables Spring Security filters to the application's web requests
public class SecurityAppConfig {

//    @Autowired
//    private TPRAuthenticationProvider tprAuthenticationProvider;
    @Autowired
    private AccountService userDetailsService;

    /**
     * Configures security rules for HTTP requests,
     * defines how requests are authorized,
     * and sets up form-based login and logout behavior
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests //URL authorization
                        .requestMatchers("/login", "/registration", "/registration-form").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .defaultSuccessUrl("/dashboard", true) // configures a login form for user authentication
                )
                .logout(logout -> logout.logoutSuccessUrl("/login")); //configures logout functionality -> redirects users to /login page on successful output

        return http.build();
    }

    /**
     * Provides a password encoder for the application,
     * uses BCrypt hashing function to encode passwords
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(userDetailsService);
//        provider.setPasswordEncoder(passwordEncoder());
//        return provider;
//    }

//    /**
//     * Registers custome auth provider, user details service, and password encoder
//     * @param http
//     * @return
//     * @throws Exception
//     */
//    @Bean
//    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
//        AuthenticationManagerBuilder authenticationManagerBuilder =
//                http.getSharedObject(AuthenticationManagerBuilder.class);
//        authenticationManagerBuilder
//                .authenticationProvider(tprAuthenticationProvider)  // Register your custom provider
//                .userDetailsService(userDetailsService)  // Use your custom UserDetailsService
//                .passwordEncoder(passwordEncoder()); // Use your custom PasswordEncoder
//        return authenticationManagerBuilder.build();
//    }

}
