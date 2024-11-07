package org.products.productreviews.web.rest;

import org.products.productreviews.ProductReviewsApplication;
import org.products.productreviews.app.entities.Review;
import org.products.productreviews.app.repositories.ReviewRepository;
import org.products.productreviews.web.patcher.Patcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;

@RestController
@RequestMapping("review")
public class ReviewAPI {

    private final ReviewRepository reviewRepo;

    ReviewAPI(ReviewRepository reviewRepository) { reviewRepo = reviewRepository; }

    /*
     * **********************
     * *** API ENDPOINTS ****
     * **********************
     */

    @PatchMapping("/edit")
    ResponseEntity<Review> editReview(@RequestBody Review incompleteReview) {
        long existingReviewID = incompleteReview.getReviewID();
        Review existingReview = reviewRepo.findById(existingReviewID).orElse(new Review());
        try{
            // Patch
            Patcher.patch(existingReview, incompleteReview);
            // Save
            reviewRepo.save(existingReview);
        } catch (Exception e) {
            ProductReviewsApplication.LOGGER.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(existingReview);
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> deleteReview(@PathVariable("id") long id) {
        Optional<Review> existingReview = reviewRepo.findById(id);
        if (existingReview.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Delete from Repo
        reviewRepo.delete(existingReview.get());
        return ResponseEntity.ok().build();
    }

}
