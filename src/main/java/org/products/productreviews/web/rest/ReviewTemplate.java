package org.products.productreviews.web.rest;

import org.products.productreviews.app.entities.Product;
import org.products.productreviews.app.entities.Review;
import org.products.productreviews.app.repositories.AccountRepository;
import org.products.productreviews.app.repositories.ProductRepository;
import org.products.productreviews.web.request.ReviewRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller that returns templates to the View that are related to Review.
 */
@Controller
@RequestMapping("/review")
public class ReviewTemplate {

    private final ProductRepository productRepo;
    private final AccountRepository accountRepo;

    ReviewTemplate(ProductRepository productRepository, AccountRepository accountRepository) {
        productRepo = productRepository;
        accountRepo = accountRepository;
    }

    /*
     * ***************************
     * *** TEMPLATE ENDPOINTS ****
     * ***************************
     */

    /**
     * Show all the Reviews owned by the current user.
     * <br>
     * <strong>W.I.P</strong>
     * @param model Model
     * @return The "selfReviews" template
     */
    @GetMapping("/owner")
    String myReviews(Model model) {
        //TODO: Retrieved logged account from auth match with repo
        model.addAttribute("reviews", "");
        return "selfReviews";
    }

    /**
     * Searches Persistence for all Reviews that are:
     * <br>
     * <ul>
     *     <li>Under a product, if productName is provided</li>
     *     <li>Under a user, if username is provided</li>
     *     <li>Under a product <strong>AND</strong> a user if both are provided</li>
     * </ul>
     *<br>
     * Example:
     * <code>
     *     /review/search?username=james?productName=Pen
     * </code>
     * <p>
     * Redirects to <code>/review/owner</code> if all request parameters are empty
     *
     * @param username Username of the User to search for
     * @param productName Product Name of the product to search for
     * @param model Spring boot Model
     * @return "reviewSearch" template
     */
    @GetMapping("/search")
    String getReviews(
            @RequestParam(name="username") Optional<String> username,
            @RequestParam(name="productName") Optional<String> productName,
            Model model) {

        String realUsername = username.orElse("");
        String realProductName = productName.orElse("");
        if (realUsername.isEmpty() && realProductName.isEmpty()) { return "redirect:/review/owner"; }
        Set<Review> reviews;

        // Determine what type of search to do
        if (!realUsername.isEmpty() && !realProductName.isEmpty()) {
            reviews = searchByUsernameAndProductName(realUsername, realProductName);
        } else if (realUsername.isEmpty()) {
            reviews = searchByProductName(realProductName);
        } else {
            reviews = searchByUsername(realUsername);
        }

        // Make sure reviews always display in some sort of consistent order.
        // Sets are unordered by nature
        ArrayList<Review> sortedReviews = reviews.stream()
                .sorted(Comparator.comparing(Review::getReviewID))
                .collect(Collectors.toCollection(ArrayList::new));

        model.addAttribute("reviews", sortedReviews);
        model.addAttribute("username", realUsername);
        model.addAttribute("productName", realProductName);
        model.addAttribute("reviewReq", new ReviewRequest());
        return "reviewSearch";
    }

    /**
     * Returns all reviews under an Account by the given username
     * @param uName The given username
     * @return All the Reviews under the Account
     */
    private Set<Review> searchByUsername(String uName) {
        return accountRepo.findByUsername(uName).getReviews();
    }

    /**
     * Returns all reviews under a Product by the given product name
     * @param pName The given product name
     * @return All the Reviews under the Product
     */
    private Set<Review> searchByProductName(String pName) {
        Product product = productRepo.findByName(pName);
        return product.getReviews();
    }

    /**
     * Returns all reviews under a Product by the given product name and Account username
     * @param uName The given username
     * @param pName The given product name
     * @return All the Reviews under the Product and Account
     */
    private Set<Review> searchByUsernameAndProductName(String uName, String pName) {
        Set<Review> reviews = searchByProductName(pName);
        return reviews.stream()
                .filter(review -> review.getAccount().getUsername().equals(uName))
                .collect(Collectors.toCollection(HashSet::new));
    }

}
