package org.products.productreviews.security;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.products.productreviews.ProductReviewsApplication;
import org.products.productreviews.app.entities.Account;
import org.products.productreviews.app.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.management.openmbean.InvalidKeyException;
import java.util.Arrays;
import java.util.logging.Level;

@Controller
public class RegistrationController {

    @Autowired
    private AccountRepository accountRepo;

    @GetMapping("/registration")
    public String showAccountRegistrationPage() {
        return "accountRegistration";
    }

    @PostMapping("/login")
    public String accountLogin(@RequestParam String username, @RequestParam String password, Model model) {
        Account account = accountRepo.findByUsername(username).orElse(null);
        if (account != null && account.getPassword().equals(password)) {
            model.addAttribute("user", account);
            return "redirect:/dashboard"; // Redirect to home page
        } else {
            model.addAttribute("error", "Invalid username or password.");
            return "userLogin"; // Reload login page with error message
        }
    }

    @PostMapping("/registration")
    public String registerAccount(@RequestParam String username,
                                  @RequestParam String password,
                                  Model model) {
        try {

            Account newAccount = Account.createAccount(accountRepo, username, password);
            accountRepo.save(newAccount);
            model.addAttribute("newAccount", newAccount);
            model.addAttribute("success", "Account created successfully.");
            //ProductReviewsApplication.LOGGER.log(Level.INFO, "New account created: " + newAccount.getUsername());
            return "redirect:/"; // Redirect to login page
        } catch (InvalidKeyException e) { //TODO: this isn't being caught
            // Handle existing username error
            model.addAttribute("error", "Username already exists. Please choose a different one.");
            return "accountRegistration"; // Reload registration form with error message

        } catch (InvalidFormatException e) {
            // Handle username/password format errors
            model.addAttribute("error", "Invalid username or password format: " + e.getMessage());
            return "accountRegistration"; // Reload registration form with error message
        }
    }

}