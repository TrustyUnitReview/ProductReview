package org.products.productreviews.web.rest;

import org.products.productreviews.app.entities.Product;
import org.products.productreviews.app.entities.Review;
import org.products.productreviews.app.repositories.ProductRepository;
import org.products.productreviews.web.request.ReviewRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

/**
 * REST controller for managing products and their reviews
 */
@Controller
@RequestMapping("/product")
public class ProductAPI {

    private ProductRepository productRepository;

    ProductAPI(ProductRepository productRepository) {productRepository = productRepository;}

    /**
     * Fetches details for a product by its ID
     *
     * @param productId The ID of the product
     * @return ResponseEntity containing the product details if found, else 404 status
     */
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> getProductDetails(@PathVariable("id") long productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            return ResponseEntity.ok(product);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product with id = " + productId + " not found");
        }
    }

    /**
     * Fetches reviews for a product by its ID.
     *
     * @param productId The ID of the product.
     * @return ResponseEntity containing the list of reviews if found, else 404 status.
     */
    @GetMapping("/{id}/reviews")
    @ResponseBody
    public ResponseEntity<?> displayProductReviews(@PathVariable("id") long productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            Set<Review> reviews = product.get().getReviews();
            return ResponseEntity.ok(reviews);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product with id = " + productId + " not found");
        }
    }

    /**
     * Submits a review for a product by its ID.
     *
     * @param productId The ID of the product.
     * @param reviewRequest The review request to turned into a review.
     * @return ResponseEntity indicating the result of the operation.
     */
    //TODO: may need to use @Transactional ?
    @PostMapping("/{id}/submitReview")
    @ResponseBody
    public ResponseEntity<?> submitReview(@PathVariable("id") long productId, @RequestBody ReviewRequest reviewRequest) {
        Optional<Product> product = productRepository.findById(productId);
        //TODO: Replace "null" with the current account. Spring Security can get the Current Account
        Review review = reviewRequest.toReview(null);
        if (product.isPresent()) {
            product.get().addReview(review);
            //TODO: Get current account, add review to current account, save account

            // Product will save review by cascade
            productRepository.save(product.get());
            return ResponseEntity.ok("Review added successfully");

        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product with id = " + productId + " not found");
        }
    }


}
