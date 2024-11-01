package org.products.productreviews.web.rest;

import org.products.productreviews.app.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/user")
public class AccountTemplate {

    @GetMapping("/{id}")
    public String viewAccount(@PathVariable long id, Model model){
        //get user from repo -> User user = userRepo.findById(id)
        model.addAttribute("user", "");
        // if the current user is authenticated, show this template -> SecurityContextHolder maybe
        return "userSelfAccount";
        //TODO: if the current user is NOT authenticated, show different template
    }

    @GetMapping("/reg")
    public String userRegistration(Model model) {
        model.addAttribute("newUser", new User());
        return "userRegistration";
    }

    @GetMapping("/login")
    public String userLogin(Model model, User user) {
        model.addAttribute("user", user);
        return "userLogin";
    }

    @PostMapping("/reg")
    public String registerAccount(Model model, User user) {
        //encrypt user password
        //userRepo.save(user)
        model.addAttribute("user", user);
        //TODO: make a home endpoint and page
        return "redirect:/";
    }
}
