package org.products.productreviews.web.rest;

import org.products.productreviews.app.entities.Product;
import org.products.productreviews.app.repositories.ProductRepository;
import org.products.productreviews.app.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.logging.Level;

import static org.products.productreviews.ProductReviewsApplication.LOGGER;

@RequestMapping("/dashboard")
@Controller
public class DashboardTemplate {

    private final ProductRepository productRepo;

    @Autowired
    private AccountService accountService;

    public DashboardTemplate(ProductRepository productRepository) {productRepo = productRepository;}

    /**
     * Return the dashboard template, sends all existing products to the view
     * @param model Model
     * @return "dashboard" template
     */
    @GetMapping
    public String dashboard(Model model) {
        ArrayList<Product> products = new ArrayList<>();
        productRepo.findAll().forEach(products::add);
        model.addAttribute("products", products);
        LOGGER.log(Level.INFO, "current user (from object): " + accountService.getCurrentUserObject().getUsername()); //TODO: remove
        return "dashboard";
    }

}
