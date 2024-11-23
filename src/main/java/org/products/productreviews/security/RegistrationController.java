package org.products.productreviews.security;

import org.products.productreviews.app.entities.Account;
import org.products.productreviews.app.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.management.openmbean.InvalidKeyException;

/**
 * Controller for handling account registration
 */
@Controller
public class RegistrationController {

    @Autowired
    private AccountRepository accountRepo;

    /**
     * Display the registration page
     * @return accountRegistration view
     */
    @GetMapping("/registration")
    public String register() {
        return "accountRegistration";
    }

    /**
     * Register a new account
     * @param model Model
     * @param username username to register
     * @param password password to register
     * @return error or success message and redirect to login page
     */
    @PostMapping("/registration")
    public String register(Model model, @RequestParam String username, @RequestParam String password) {
        try {
            Account acc = Account.createAccount(accountRepo, username, password);
            accountRepo.save(acc);
            model.addAttribute("success", "Account created successfully");
        } catch (InvalidKeyException e) {
            model.addAttribute("error", "Username is already in use");
            return "accountRegistration";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "accountRegistration";
        }catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }

        return "redirect:/login";
    }

}