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

@Controller
@RequestMapping("/product")
public class ProductAPI {

    /**
     * TODO: Product Page
     * Uses cases:
     *  - I want to view product details (name, description, price, reviews, image)
     *  - I want to view reviews of product
     *  - I want to submit a review of the product
     *  - After submitting a review, the list of reviews for product should be updated
     */

    @Autowired
    private ProductRepository productRepository;

    /**
     * Fetches details for a product by its ID
     * @param productId The ID of the product
     * @return Product details if found, else 404 status
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

    //TODO: may need to use @Transactional ?
    @PostMapping("/{id}/review")
    @ResponseBody
    public ResponseEntity<?> submitReview(@RequestParam(value = "id") long productId, @RequestBody Review review) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            product.get().addReview(review); //TODO: where does the review get created?
            productRepository.save(product.get());
            return ResponseEntity.ok("Review added successfully");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product with id = " + productId + " not found");
        }
    }


}
