package org.products.productreviews.security;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.Valid;
import org.products.productreviews.ProductReviewsApplication;
import org.products.productreviews.app.entities.Account;
import org.products.productreviews.app.entities.DTOs.AccountCreationDTO;
import org.products.productreviews.app.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.management.openmbean.InvalidKeyException;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;

@Controller
public class RegistrationController {

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/registration")
    public String register(Model model) {
        AccountCreationDTO accountCreationDTO = new AccountCreationDTO();
        model.addAttribute("accountCreationDTO", accountCreationDTO);
        model.addAttribute("success", false);
        return "accountRegistration";
    }

    @PostMapping("/registration")
    public String register(Model model, @Valid @ModelAttribute AccountCreationDTO accountCreationDTO, BindingResult result) {
        if (!accountCreationDTO.getPassword().equals(accountCreationDTO.getConfirmPassword())) {
            result.addError(new FieldError("accountCreationDTO", "confirmPassword", "Passwords do not match"));
        }

        Optional<Account> account = accountRepo.findByUsername(accountCreationDTO.getUsername());
        if (account.isPresent()) {
            result.addError(new FieldError("accountCreationDTO", "username", "Username is already in use"));
        }

        if (result.hasErrors()) {
            return "accountRegistration";
        }

        try {
            var bCryptPassword = new BCryptPasswordEncoder();
            String password = bCryptPassword.encode(accountCreationDTO.getPassword());
            Account.createAccount(accountRepo, accountCreationDTO.getUsername(), password);

            model.addAttribute("accountCreationDTO", new AccountCreationDTO());
            model.addAttribute("success", true);

        } catch (Exception e) {
            result.addError(new FieldError("accountCreationDTO", "username", e.getMessage()));
        }

        return "accountRegistration";
    }

    @GetMapping("/")
    public String accountLogin(Model model) {
        return "userLogin"; // Login form
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "userLogin"; // Login form
    }

    @PostMapping("/login")
    public String accountLogin(@RequestParam String username, @RequestParam String password, Model model) {
        Account account = accountRepo.findByUsername(username).orElse(null);
        if (account != null && passwordEncoder.matches(password, account.getPassword())) {
            model.addAttribute("user", account);
            return "redirect:/dashboard"; // Redirect to home page
        } else {
            System.out.println("FAIL");
            model.addAttribute("error", "Invalid username or password.");
            return "userLogin"; // Reload login page with error message
        }
    }
}
