package org.products.productreviews.web.rest;

import jakarta.servlet.http.HttpServletRequest;
import org.products.productreviews.app.entities.Account;
import org.products.productreviews.app.repositories.AccountRepository;
import org.products.productreviews.web.util.WebUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

/**
 * REST controller for managing accounts
 */
@Controller
@RequestMapping("/account")
public class AccountAPI {

    private final AccountRepository accountRepo;

    /**
     * Constructor for initializing account repository
     *
     * @param accountRepo The repository for accounts
     */
    public AccountAPI(AccountRepository accountRepo) {
        this.accountRepo = accountRepo;
    }

    /**
     * Updates an account to add a following to another user
     *
     * @param username of the account to be followed
     * @param request HttpServletRequest reference
     * @return The page to be served upon following an account
     */
    @PostMapping("/{username}/follow")
    public String followAccount(@PathVariable("username") String username, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Account> account = accountRepo.findByUsername(authentication.getName());
        Optional<Account> otherAccount = accountRepo.findByUsername(username);

        if (account.isPresent() && otherAccount.isPresent()) {
            account.get().addFollows(otherAccount.get());
            accountRepo.save(account.get());
            return WebUtil.getPreviousPageByRequest(request).orElse("/dashboard");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found").toString();
        }
    }

    /**
     * Updates an account to remove a user from following
     *
     * @param username of the account to be unfollowed
     * @param request HttpServletRequest reference
     * @return The page to be served upon unfollowing an account
     */
    @PostMapping("/{username}/unfollow")
    public String unfollowAccount(@PathVariable("username") String username, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Account> account = accountRepo.findByUsername(authentication.getName());
        Optional<Account> otherAccount = accountRepo.findByUsername(username);

        if (account.isPresent() && otherAccount.isPresent()) {
            account.get().removeFollows(otherAccount.get());
            accountRepo.save(account.get());
            return WebUtil.getPreviousPageByRequest(request).orElse("/dashboard");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found").toString();
        }
    }
}
