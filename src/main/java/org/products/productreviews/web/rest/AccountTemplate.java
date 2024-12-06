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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
     * Get top 5 followed accounts in the system
     */
    private List<Account> getTop5Followed(Account ownAccount) {
        List<Account> allAccounts = new ArrayList<>();
        // I'm sure there's a way to do it in one lambda, but it'll be more readable by splitting the steps
        accountRepo.findAll().forEach(allAccounts::add);
        // Exclude yourself, and any follows from list
        allAccounts.remove(ownAccount);
        ownAccount.getFollows().forEach(allAccounts::remove);
        // Sort by following amount -> we want decreasing order
        allAccounts.sort(Comparator.comparingInt(Account::getFollowerCount).reversed());

        // Up to five followed
        int listSize = Math.min(allAccounts.size(), 5);
        return allAccounts.subList(0, listSize);
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
            model.addAttribute("topFollowList", getTop5Followed(selfAccount));
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
