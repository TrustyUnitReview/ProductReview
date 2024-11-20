package org.products.productreviews.security;

import org.products.productreviews.app.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Custom authentication provider for the application.
 */
@Component
public class TPRAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = authentication.getPrincipal().toString();
        final String password = authentication.getCredentials().toString();

        if(accountRepository.existsByUsername(username)) { //TODO: need to add password verification
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER")); //tells Spring Security that this user has the role
            return new UsernamePasswordAuthenticationToken(username, password, authorities); //simple username - password authentication
        }
        else {
            throw new BadCredentialsException("Username or password does not exist! username: " + username + " password: " + password); //TODO: take out creds
        }

        //return authentication;
    }

    /**
     * Specifies the type of Authentication object (to UsernamePasswordAuthenticationToken) this provider can authenticate.
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
