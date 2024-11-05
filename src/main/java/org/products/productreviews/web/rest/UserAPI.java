package org.products.productreviews.web.rest;

import org.products.productreviews.ProductReviewsApplication;
import org.products.productreviews.app.entities.Account;
import org.products.productreviews.web.patcher.Patcher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.logging.Level;

@Controller
@RequestMapping("/user")
public class UserAPI {

    @PatchMapping("/{id}")
    public ResponseEntity<String> editUser(@PathVariable long id, @RequestBody Account partialAccount) {
        Account currentAccount = null;
        //find user from userRepo by id and ensure user matches with authenticated user
        // if user does not match, don't patch
        if (currentAccount != null) {
            try {
                Patcher.patch(currentAccount, partialAccount); //this approach is simple, but maybe DTO pattern
                //save to repo
            } catch (IllegalAccessException e) {
                ProductReviewsApplication.LOGGER.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id) {

        // find account by id
        Account account = null;
        //authenticate  current account with found account
        // if account does not match, don't delete
        if (account != null) {
            //userRepo.deleteById(id)
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

}
