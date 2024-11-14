package org.products.productreviews.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityAppConfig {

    /**
     * Defines which URL paths should be secured and which should not
     * @param http URL being analyzed
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        //Allows any request to /login or /reg and any other authenticated request
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/reg").permitAll()
                        .anyRequest().authenticated()
                ).httpBasic(Customizer.withDefaults())
                //Redirects to "/" on successful login TODO: Route to dashboard
                .formLogin(form -> form.defaultSuccessUrl("/", true))
                //Redirects to log in after successful logout
                .logout(logout -> logout.logoutSuccessUrl("/login"));

        return http.build();
    }
}
