package org.products.productreviews;

import org.products.productreviews.app.entities.Product;
import org.products.productreviews.app.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.logging.Logger;

@SpringBootApplication
public class ProductReviewsApplication {

    public static Logger LOGGER = Logger.getLogger("ProductReviewsApplication");


    public static void main(String[] args) {
        SpringApplication.run(ProductReviewsApplication.class, args);
    }

    // REMOVE WHEN NOT NEEDED
    @Bean
    CommandLineRunner commandLineRunner(ProductRepository productRepository) {
        return args -> Product.createProduct(productRepository,
                "Fountain Pen",
                399.99f,
                "A Fountain Pen",
                null);
    }

}
