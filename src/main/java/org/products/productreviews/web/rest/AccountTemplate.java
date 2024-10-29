package org.products.productreviews.web.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/user")
public class AccountTemplate {

    @GetMapping("/{id}")
    public String viewAccount(@PathVariable long id, Model model){
        //get user from repo -> User user = userRepo.findById(id)
        model.addAttribute("user", "");
        return "userAccount";
    }

}
