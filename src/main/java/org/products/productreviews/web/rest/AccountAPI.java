package org.products.productreviews.web.rest;

import org.products.productreviews.ProductReviewsApplication;
import org.products.productreviews.app.User;
import org.products.productreviews.web.patcher.Patcher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.logging.Level;

@Controller
@RequestMapping("/user")
public class AccountAPI {

    @PatchMapping("/{id}")
    public ResponseEntity<String> editAccount(@PathVariable long id, @RequestBody User partialUser) {
        //find user from userRepo by id and ensure user matches with authenticated user
        // if not, reject the patch request
        User currentUser = null;
        try {
            Patcher.patch(currentUser, partialUser, User.class); //this approach is simpler, but maybe DTO pattern
            //save to repo
        } catch (IllegalAccessException e) {
            ProductReviewsApplication.LOGGER.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
}
