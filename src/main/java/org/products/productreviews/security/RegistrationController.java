package org.products.productreviews.security;

import org.products.productreviews.app.entities.Account;
import org.products.productreviews.app.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class RegistrationController {

    @Autowired
    private AccountRepository accountRepo;


    @GetMapping("/registration")
    public String register(Model model) {
        return "accountRegistration";
    }

    @PostMapping("/registration")
    public String register(Model model, @RequestParam String username, @RequestParam String password) { //TODO: I don't think we need the client side validation if we have this
        var passwordEncoder = new BCryptPasswordEncoder();

        Optional<Account> account = accountRepo.findByUsername(username);
        if (account.isPresent()) {
            model.addAttribute(new FieldError("username", "username", "Username is already in use"));
        }

        try {
            String passwordCoded = passwordEncoder.encode(password);
            Account acc = Account.createAccount(accountRepo, username, passwordCoded);
            accountRepo.save(acc);

        } catch (Exception e) {
            model.addAttribute(new FieldError("accountCreationDTO", "username", e.getMessage()));
        }

        return "redirect:/login";
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

}