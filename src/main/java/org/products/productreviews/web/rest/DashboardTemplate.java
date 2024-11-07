package org.products.productreviews.web.rest;

import org.products.productreviews.app.entities.Product;
import org.products.productreviews.app.repositories.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@RequestMapping("/dashboard")
@Controller
public class DashboardTemplate {

    private final ProductRepository productRepo;

    public DashboardTemplate(ProductRepository productRepository) {productRepo = productRepository;}

    @GetMapping
    public String dashboard(Model model) {
        ArrayList<Product> products = new ArrayList<>();
        productRepo.findAll().forEach(products::add);
        model.addAttribute("products", products);
        return "dashboard";
    }

}
