package org.products.productreviews.web.rest;

import org.products.productreviews.app.entities.Account;
import org.products.productreviews.app.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.Arrays;
import java.util.Optional;

/**
 * Controller responsible for serving views
 */
@Controller
@RequestMapping("/account")
public class AccountTemplate {
    private final AccountRepository accountRepo;

    /**
     * Constructor for initializing account repository
     *
     * @param accountRepo The repository for accounts
     */
    public AccountTemplate(AccountRepository accountRepo) {
        this.accountRepo = accountRepo;
    }

    /**
     * Serves the current user's page
     *
     * @param model Model
     * @return The page corresponding to the current user
     */
    @GetMapping("/own")
    public String viewAccount(Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Account account = accountRepo.findByUsername(authentication.getName());
//        model.addAttribute("account", account);
        return "selfAccount";
    }

    /**
     * Serves the page for all account other than current user. If current user is used, redirects to /own.
     *
     * @param username The username of the other account
     * @param model Model
     * @return The page corresponding to the account. If current user is used, redirects to /own.
     */
    @GetMapping("/{username}")
    public String viewAccount(@PathVariable String username, Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Account account = accountRepo.findByUsername(authentication.getName());
//        model.addAttribute("account", account);
//        if (username.equals(account.getUsername())) {
//            return "redirect:/account/own";
//        }

        Optional<Account> otherAccount = accountRepo.findByUsername(username);
        model.addAttribute("otherAccount", otherAccount);
        return "otherAccount";
    }

}
