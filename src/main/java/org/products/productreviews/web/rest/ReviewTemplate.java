package org.products.productreviews.web.rest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/review")
public class ReviewTemplate {

    // private final UserRepo userRepo;
    // private final ReviewRepo reviewRepo;

    /*
     * ***************************
     * *** TEMPLATE ENDPOINTS ****
     * ***************************
     */

    @GetMapping("/owner")
    String myReviews(Model model) {
        // Retrieved logged account from auth match with repo
        model.addAttribute("reviews", "");
        return "selfReviews";
    }

    @GetMapping("/search")
    String getReview(@RequestParam(name="username") String username, Model model) {
        // From UserRepo get users with same name, they will contain reviews
        model.addAttribute("users", "");
        return "reviewSearch";
    }

}
