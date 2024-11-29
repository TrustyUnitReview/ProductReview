package org.products.productreviews.web.rest;

import org.products.productreviews.ProductReviewsApplication;
import org.products.productreviews.app.entities.Account;
import org.products.productreviews.app.entities.Product;
import org.products.productreviews.app.entities.Review;
import org.products.productreviews.app.repositories.AccountRepository;
import org.products.productreviews.app.repositories.ProductRepository;
import org.products.productreviews.web.util.ProductCategory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;

@RequestMapping("/dashboard")
@Controller
public class DashboardTemplate {

    private final ProductRepository productRepo;
    private final AccountRepository accountRepo;
    public DashboardTemplate(ProductRepository productRepository, AccountRepository accountRepository) {
        productRepo = productRepository;
        accountRepo = accountRepository;
    }

    /**
     * Return the dashboard template, sends all existing products to the view
     * @param model Model
     * @return "dashboard" template
     */
    @GetMapping
    public String dashboard(@RequestParam(name = "category", required = false) String categoryName,
                            @RequestParam(name = "sortByUsersFollowing", required = false) boolean sortByUsersFollowing,
                            Model model) {
        List<Product> products;
        //if user selects a category from dropdown
        if (categoryName != null && !categoryName.isEmpty()) {
            products = sortProductsByCategory(categoryName);
            products = sortProductsAvgRating(products); //sort products by avg rating
            //if user selects to sort by users they follow
            if(sortByUsersFollowing){
                products = sortProductsAvgRatingUsersFollowing(products);
            }
        } else {
            // no category selected, show all products
            products = (List<Product>) productRepo.findAll();
        }

        model.addAttribute("products", products);
        model.addAttribute("sortByUsersFollowing", sortByUsersFollowing);
        model.addAttribute("categories", Arrays.asList(ProductCategory.values())); // Pass categories for dropdown

        return "dashboard";
    }

    private List<Product> sortProductsByCategory(String category){
        List<Product> products;
        try {
            ProductCategory productCategory = ProductCategory.fromDisplayName(category);
            products = productRepo.findByCategory(productCategory);
        } catch (IllegalArgumentException e) {
            // invalid category name, show all products or handle the error
            products = (List<Product>) productRepo.findAll();
        }
        return products;
    }

    /**
     * Sort products by average rating
     * @param products Products to sort (after category filter has been applied)
     * @return Sorted products
     */
    private List<Product> sortProductsAvgRating(List<Product> products){
        products.sort((p1, p2) -> Double.compare(p2.getAvgReviewScore(), p1.getAvgReviewScore()));
        return products;
    }

    private List<Product> sortProductsAvgRatingUsersFollowing(List<Product> products){
        //get current user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Account> account = accountRepo.findByUsername(authentication.getName());
        if (account.isPresent()) {
            Account currUser = account.get();

            //get people user follows
            Set<Account> followedUsers = currUser.getFollows();
            ProductReviewsApplication.LOGGER.log(Level.INFO, "Followed Users:");
            for(Account user : followedUsers){
                ProductReviewsApplication.LOGGER.log(Level.INFO, user.getUsername());
            }

            //calculate average rating for each product based on ratings from followed users
            //sort products based on calculated average ratings
            products.sort((p1, p2) -> {
                double avgRatingP1 = calculateAverageRatingFromFollowedUsers(p1, followedUsers);
                double avgRatingP2 = calculateAverageRatingFromFollowedUsers(p2, followedUsers);
                return Double.compare(avgRatingP2, avgRatingP1); // Descending order
            });
        }
        return products;
    }

    /**
     * Calculate the average rating of a product based on ratings from followed users
     * @param product Product to calculate average rating for
     * @param followedUsers Set of users that the current user follows
     * @return Average rating from followed users
     */
    private float calculateAverageRatingFromFollowedUsers(Product product, Set<Account> followedUsers) {
        Set<Review> reviews = product.getReviews(); // Assuming Product has a `getReviews()` method
        float totalRating = 0.0f;
        int count = 0;
        float avg = 0.0f;

        // go through each review of product and check if a followed user left a review
        for (Review review : reviews) {
            if (followedUsers.contains(review.getAccount())) {
                //if a followed user left a review, add the rating to totalRating
                ProductReviewsApplication.LOGGER.log(Level.INFO, "Review by followed user: " + review.getAccount().getUsername());
                totalRating += review.getRating().getValue();
                count++;
            }
        }
        //calculate average rating
        if(count > 0){
            avg = totalRating / count;
        }
        ProductReviewsApplication.LOGGER.log(Level.INFO, "Average rating from followed users: " + avg);
        return avg;
    }




}
