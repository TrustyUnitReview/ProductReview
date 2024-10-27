package org.products.productreviews.web;

import org.products.productreviews.ProductReviewsApplication;
import org.products.productreviews.app.Review;
import org.products.productreviews.web.patcher.ReviewPatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;


@RestController
@RequestMapping("/review")
public class Reviews {

    // private final UserRepo userRepo;
    // private final ReviewRepo reviewRepo;

    /*
     * ***************************
     * *** TEMPLATE ENDPOINTS ****
     * ***************************
     */

    @GetMapping("/owner")
    public String myReviews(Model model) {
        // Retrieved logged account from auth match with repo
        model.addAttribute("reviews", "");
        return "selfReviews";
    }

    @GetMapping("/search")
    public String getReview(@RequestParam(name="username") String username, Model model) {
        // From UserRepo get users with same name, they will contain reviews
        model.addAttribute("users", "");
        return "reviewSearch";
    }

    /*
     * **********************
     * *** API ENDPOINTS ****
     * **********************
     */

    @PatchMapping("/edit")
    public ResponseEntity<Review> editReviewAPI(@RequestBody Review incompleteReview) {
        // Find existing Review in DB reviewRepo.findByID(incompleteReview.getID())
        Review existingReview = null;
        try{
            // Patch
            ReviewPatcher.reviewPatch(existingReview, incompleteReview);
            // Save
            // ReviewRepo.save(existingIntern);
        } catch (Exception e) {
            ProductReviewsApplication.LOGGER.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(existingReview);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> editReviewAPI(@PathVariable("id") int id) {
        Optional<Review> existingReview = null; // Find By ID
        if (existingReview.isPresent()) {
            // Delete from Repo
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createReviewAPI(@RequestBody Review completeReview) {
        // Add to repository
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
