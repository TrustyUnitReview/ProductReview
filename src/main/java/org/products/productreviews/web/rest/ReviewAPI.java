package org.products.productreviews.web.rest;

import jakarta.servlet.http.HttpServletRequest;
import org.products.productreviews.ProductReviewsApplication;
import org.products.productreviews.app.entities.Account;
import org.products.productreviews.app.entities.Product;
import org.products.productreviews.app.entities.Review;
import org.products.productreviews.app.repositories.AccountRepository;
import org.products.productreviews.app.repositories.ProductRepository;
import org.products.productreviews.app.repositories.ReviewRepository;
import org.products.productreviews.web.request.ReviewRequest;
import org.products.productreviews.web.util.WebUtil;
import org.products.productreviews.web.util.patcher.Patcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;

/**
 * REST controller for manipulating Reviews
 */
@Controller
@RequestMapping("review")
public class ReviewAPI {

    private final ProductRepository productRepo;
    private final AccountRepository accountRepo;
    private final ReviewRepository reviewRepo;

    ReviewAPI(ProductRepository productRepository,
               AccountRepository accountRepository,
               ReviewRepository reviewRepository) {
        productRepo = productRepository;
        accountRepo = accountRepository;
        reviewRepo = reviewRepository;
    }

    /*
     * **********************
     * *** API ENDPOINTS ****
     * **********************
     */

    /**
     * Edit a Review object or create a new one if one doesn't exist.
     * @param incompleteReview The incomplete review object with the desired changes
     * @return The final review after all changes have been applied.
     */
    @PatchMapping("/editJSON")
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

    /**
     * Edit review belonging to current user under specified product
     * @param request HttpRequest
     * @param name Name of the product
     * @param reviewRequest ReviewRequest object
     * @return Redirect to where the request came from.
     */
    @PostMapping("/{name}/edit")
    String editReviewByProductName(
            HttpServletRequest request,
            @PathVariable("name") String name,
            @ModelAttribute ReviewRequest reviewRequest) throws IllegalAccessException {

        Product product = productRepo.findByName(name);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Account> accountOptional = accountRepo.findByUsername(authentication.getName());

        if (accountOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found").toString();
        }

        Account account =  accountOptional.get();

        Optional<Review> reviewOptional = account.getReviews().stream()
                .filter(review -> review.getProduct().equals(product))
                .findFirst();

        if (reviewOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review to be edited not found").toString();
        }

        Review reviewToEdit = reviewOptional.get();
        Patcher.patch(reviewToEdit, reviewRequest.toReview(account));
        reviewRepo.save(reviewToEdit);
        return WebUtil.getPreviousPageByRequest(request).orElse("/dashboard");
    }

    /**
     * Delete a review
     * @param id ID of the review to be deleted
     * @return HTTP OK is ID exists. HTTP NOT FOUND if the ID doesn't exist.
     */
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
