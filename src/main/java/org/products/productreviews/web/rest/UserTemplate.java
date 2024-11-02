package org.products.productreviews.web.rest;

import org.products.productreviews.app.Entities.User;
import org.products.productreviews.app.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import java.util.Arrays;

@Controller
@RequestMapping("/user")
public class UserTemplate {
    // TODO: Inspect this, is there a better way to do this?
    //  Removing repo from createUser would fix the issue at the root.
    @Autowired
    private UserRepository repo;


    @GetMapping("/{id}")
    public String viewUser(@PathVariable long id, Model model){
        //get user from repo -> User user = userRepo.findById(id)
        model.addAttribute("user", "");
        // if the current user is authenticated, show this template -> SecurityContextHolder maybe
        return "userSelfUser"; // TODO: renamed account to user, not sure if this name fits anymore.
        //TODO: if the current user is NOT authenticated, show different template
    }

    @GetMapping("/reg")
    public String userRegistration(Model model) {
        // TODO: Add input for username, password
        try{
            model.addAttribute("newUser", User.createUser(repo, "username", "password"));
        }
        catch (Exception e){
            // TODO: Better error handler, have a way to retry registration? -> Different behavior based on type?
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return "userRegistration";
    }

    @GetMapping("/login")
    public String userLogin(Model model, User user) {
        model.addAttribute("user", user);
        return "userLogin";
    }

    @PostMapping("/reg")
    public String registerUser(Model model, User user) {
        //encrypt user password
        //userRepo.save(user)
        model.addAttribute("user", user);
        //TODO: make a home endpoint and page
        return "redirect:/";
    }
}
