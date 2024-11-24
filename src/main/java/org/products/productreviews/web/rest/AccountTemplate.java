package org.products.productreviews.web.rest;

import org.products.productreviews.app.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/account")
public class AccountTemplate {
    // TODO: Inspect this, is there a better way to do this?
    //  Removing repo from createUser would fix the issue at the root.
    //TODO: I think we should create a security folder and move all security related classes there

    @Autowired
    private AccountRepository accountRepo;

    @GetMapping("/{id}")
    public String viewAccount(@PathVariable long id, Model model){
        //get user from repo -> Account user = userRepo.findById(id)
        model.addAttribute("user", "");
        // if the current user is authenticated, show this template -> SecurityContextHolder maybe
        return "userSelfUser"; // TODO: renamed account to user, not sure if this name fits anymore.
        //TODO: if the current user is NOT authenticated, show different template
    }

}
