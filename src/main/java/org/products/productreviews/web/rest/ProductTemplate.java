package org.products.productreviews.web.rest;

import org.products.productreviews.app.entities.Account;
import org.products.productreviews.app.entities.Product;
import org.products.productreviews.app.entities.Review;
import org.products.productreviews.app.repositories.AccountRepository;
import org.products.productreviews.app.repositories.ProductRepository;
import org.products.productreviews.app.util.JDistance;
import org.products.productreviews.web.request.ReviewRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final AccountRepository accountRepo;

    ProductTemplate(ProductRepository productRepository, AccountRepository accountRepository) {
        productRepo = productRepository;
        accountRepo = accountRepository;
    }

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

    /**
     * Returns information about a specific product suitable for the show product template, the reviews are filtered.
     * @param pName The product's unique name
     * @param sortType The sort type to use. (Currently only "Jaccard")
     * @param model The Model
     * @return The showProduct template
     */
    @GetMapping("/filter")
    String getProductPageSort(
            @RequestParam(name="productName") String pName,
            @RequestParam(name="sort") String sortType,
            Model model){

        // Only works for Jaccard. if not Jaccard go back to dashboard.
        // TODO: Show error page instead.
        if (!sortType.equals("Jaccard")){
            return "dashboard";
        }

        String template = getProductPage(pName, model);
        ArrayList<?> maybeReviews = (ArrayList<?>) model.getAttribute("reviews");
        ArrayList<Review> jaccardSortedReviews = jaccardSort(maybeReviews);

        model.addAttribute("reviews", jaccardSortedReviews);

        return template;
    }

    /**
     * Sorts the reviews according the Jaccard distance between the current use and the Review's owner.
     * @param potentiallyReviews ArrayList of Reviews
     * @return The Jaccard sorted list of Reviews
     * @throws RuntimeException If the arraylist is not in the format expected by the method.
     */
    ArrayList<Review> jaccardSort(ArrayList<?> potentiallyReviews) throws RuntimeException{
        if (potentiallyReviews != null && potentiallyReviews.isEmpty()){
            throw new RuntimeException("No Reviews Found to be sorted!");
        } else if (!(potentiallyReviews.get(0) instanceof Review)) {
            throw new RuntimeException("List acquired from model did not contain Review objects");
        }

        @SuppressWarnings("unchecked") // This was checked above. Suppressing IDE warnings.
        ArrayList<Review> reviews = (ArrayList<Review>) new ArrayList<>(potentiallyReviews);
        reviews.sort(Comparator.comparing(this::comparingJaccardScore).reversed());

        return reviews;

    }

    /**
     * Comparator Function for Jaccard Distance for a review.
     * @param review The Review to be compared
     * @return The Jaccard distance.
     */
    float comparingJaccardScore(Review review){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = accountRepo.findByUsername(authentication.getName()).orElseThrow();

        JDistance jDistance = new JDistance(account, review.getAccount());
        return jDistance.distance();
    }


}
