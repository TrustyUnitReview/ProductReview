package org.products.productreviews.web.rest;

import jakarta.servlet.http.HttpServletRequest;
import org.products.productreviews.ProductReviewsApplication;
import org.products.productreviews.app.entities.Account;
import org.products.productreviews.app.repositories.AccountRepository;
import org.products.productreviews.web.util.WebUtil;
import org.products.productreviews.web.util.patcher.Patcher;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

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
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Account account = accountRepo.findByUsername(authentication.getName());
        Account otherAccount = accountRepo.findByUsername(username);

//        account.addFollows(otherAccount);
//        accountRepo.save(account);
        return WebUtil.getPreviousPageByRequest(request).orElse("/dashboard");
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
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Account account = accountRepo.findByUsername(authentication.getName());
        Account otherAccount = accountRepo.findByUsername(username);

//        account.removeFollows(otherAccount);
//        accountRepo.save(account);
        return WebUtil.getPreviousPageByRequest(request).orElse("/dashboard");
    }
}
