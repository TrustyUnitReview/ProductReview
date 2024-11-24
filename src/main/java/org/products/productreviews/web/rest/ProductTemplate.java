package org.products.productreviews.web.rest;

import org.products.productreviews.app.entities.Product;
import org.products.productreviews.app.entities.Review;
import org.products.productreviews.app.repositories.ProductRepository;
import org.products.productreviews.web.request.ReviewRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Controller that returns templates to the View that are related to Product.
 */
@Controller
@RequestMapping("/product")
public class ProductTemplate {

    private final ProductRepository productRepo;

    ProductTemplate(ProductRepository productRepository) {productRepo = productRepository;}

    /**
     * Returns information about a specific product suitable for the show product template.
     * @param pName The product's unique name
     * @param model The Model
     * @return The showProduct template
     */
    @GetMapping("/show")
    String getProductPage(@RequestParam(name="productName") String pName, Model model){
        Product product = productRepo.findByName(pName);

        ArrayList<Review> sortedReviews = product.getReviews().stream()
                .sorted(Comparator.comparing(Review::getReviewID))
                .collect(Collectors.toCollection(ArrayList::new));

        model.addAttribute("product", product);
        model.addAttribute("productName", pName);
        model.addAttribute("reviews", sortedReviews);
        model.addAttribute("reviewReq", new ReviewRequest());
        return "showProduct";
    }


}
