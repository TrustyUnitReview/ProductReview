package org.products.productreviews.app.services;

import org.products.productreviews.app.entities.Account;
import org.products.productreviews.app.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepo;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepo = accountRepository;
    }

    /**
     * Loads specific user details by username during authentication
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> account = accountRepo.findByUsername(username);
        if (account.isPresent()) {
            var accountObj = account.get();
            return User.builder()
                    .username(accountObj.getUsername())
                    .password(accountObj.getPassword())
                    .build();
        }else{
            throw new UsernameNotFoundException(username);
        }
    }
}
