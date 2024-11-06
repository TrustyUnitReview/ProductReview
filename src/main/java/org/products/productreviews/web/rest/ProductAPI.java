package org.products.productreviews.web.rest;

import org.products.productreviews.app.entities.Product;
import org.products.productreviews.app.entities.Review;
import org.products.productreviews.app.repositories.ProductRepository;
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

    @Autowired
    private ProductRepository productRepository;

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
     * @param review The review to be added.
     * @return ResponseEntity indicating the result of the operation.
     */
    //TODO: may need to use @Transactional ?
    @PostMapping("/{id}/submitReview")
    @ResponseBody
    public ResponseEntity<?> submitReview(@PathVariable("id") long productId, @RequestBody Review review) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            product.get().addReview(review);
            //TODO: where does the review get created?
            //TODO: review needs to save this product
            //TODO: account needs to add review to their set
            productRepository.save(product.get());
            return ResponseEntity.ok("Review added successfully");

        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product with id = " + productId + " not found");
        }
    }


}
