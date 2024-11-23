package org.products.productreviews.app.services;

import org.products.productreviews.app.entities.Account;
import org.products.productreviews.app.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * UserDetailsService interface abstracts the retrieval of user details for Spring Security's authentication process
 */
@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepo;

    /**
     * Loads specific user details by username during authentication.
     * When a user tries to authenticate, this method receives the username, searches the database for a record containing it, and returns a UserDetails object.
     * @param username username of the user
     * @return UserDetails object
     * @throws UsernameNotFoundException if the username is not found in the database
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> account = accountRepo.findByUsername(username);
        if (account.isPresent()) {
            var accountObj = account.get();
            return User.builder() //returns a UserDetails object, populated with the username and hashed password
                    .username(accountObj.getUsername())
                    .password(accountObj.getPassword())
                    .build();
        }else{
            throw new UsernameNotFoundException(username);
        }
    }

    /**
     * Grabs the UserDetails object of the currently authenticated user
     * @return UserDetails object
     */
    public UserDetails getCurrentUserObject() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            return (UserDetails) authentication.getPrincipal(); //return authentication.getName() if you just want name
        }

        return null; // Return null if no user is authenticated
    }


}
