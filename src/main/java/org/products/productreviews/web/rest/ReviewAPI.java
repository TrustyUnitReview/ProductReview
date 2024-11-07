package org.products.productreviews.web.rest;

import org.products.productreviews.ProductReviewsApplication;
import org.products.productreviews.app.entities.Review;
import org.products.productreviews.app.repositories.ReviewRepository;
import org.products.productreviews.web.patcher.Patcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;

@RestController
@RequestMapping("review")
public class ReviewAPI {

    private ReviewRepository reviewRepo;

    ReviewAPI(ReviewRepository reviewRepo) { this.reviewRepo = reviewRepo; }

    /*
     * **********************
     * *** API ENDPOINTS ****
     * **********************
     */

    @PatchMapping("/edit")
    ResponseEntity<Review> editReviewAPI(@RequestBody Review incompleteReview) {
        // Find existing Review in DB reviewRepo.findByID(incompleteReview.getID())
        Review existingReview = null;
        try{
            // Patch
            Patcher.patch(existingReview, incompleteReview);
            // Save
            // ReviewRepo.save(existingIntern);
        } catch (Exception e) {
            ProductReviewsApplication.LOGGER.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(existingReview);
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> editReviewAPI(@PathVariable("id") int id) {
        Optional<Review> existingReview = null; // Find By ID
        if (existingReview.isPresent()) {
            // Delete from Repo
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    //TODO: not sure if there should be a method here to assign a review to a product that has review.setProduct(product.get())

    @PostMapping("/create")
    ResponseEntity<Void> createReviewAPI(@RequestBody Review completeReview) {
        // Add to repository
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
