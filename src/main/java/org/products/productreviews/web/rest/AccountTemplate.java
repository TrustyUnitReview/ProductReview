package org.products.productreviews.web.rest;

import org.products.productreviews.app.entities.Account;
import org.products.productreviews.app.repositories.AccountRepository;
import org.products.productreviews.app.util.AccountSeparation;
import org.products.productreviews.app.util.JDistance;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Account> account = accountRepo.findByUsername(authentication.getName());
        if (account.isPresent()) {
            JDistance distance = new JDistance();
            Account selfAccount = account.get();
            distance.setAccountA(selfAccount);

            model.addAttribute("account", selfAccount);
            model.addAttribute("JDistance", distance);
            return "selfAccount";
        } else {
            return "redirect:/dashboard";
        }
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Account> account = accountRepo.findByUsername(authentication.getName());
        Optional<Account> otherAccount = accountRepo.findByUsername(username);

        if (account.isPresent() && otherAccount.isPresent()) {
            if (username.equals(account.get().getUsername())) {
                return "redirect:/account/own";
            }
            int degree = AccountSeparation.getSeparation(account.get(), otherAccount.get());

            model.addAttribute("degree", degree);
            model.addAttribute("account", account.get());
            model.addAttribute("otherAccount", otherAccount.get());
            return "otherAccount";
        } else {
            return "redirect:/dashboard";
        }
    }
}
