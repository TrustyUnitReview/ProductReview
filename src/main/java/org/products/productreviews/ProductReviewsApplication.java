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

    // TODO: REMOVE WHEN NOT NEEDED

    /**
     * Runner that will create products for using the application.
     * Ideally this is done through a View and by the request of a user, this method exists to bridge the gap between
     * now and when this feature is finished.
     * @param productRepository Autowired product repository
     * @return CommandLineRunner
     */
    @Bean
    CommandLineRunner commandLineRunner(ProductRepository productRepository) {
        return args ->  {
            Product example = Product.createProduct(productRepository,
                "FountainPen",
                399.99f,
                "A Fountain Pen",
                null);
            Product example1 = Product.createProduct(productRepository,
                    "FountainPen2",
                    399.99f,
                    "A Fountain Pen",
                    null);
            Product example2 = Product.createProduct(productRepository,
                    "FountainPen3",
                    399.99f,
                    "A FountainPen",
                    null);
            Product example3 = Product.createProduct(productRepository,
                    "FountainPen4",
                    399.99f,
                    "A Fountain Pen",
                    null);
            productRepository.save(example);
            productRepository.save(example1);
            productRepository.save(example2);
            productRepository.save(example3);

        };
    }

}
