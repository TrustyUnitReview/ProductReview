package org.products.productreviews;

import org.products.productreviews.app.entities.Account;
import org.products.productreviews.app.entities.Product;
import org.products.productreviews.app.repositories.AccountRepository;
import org.products.productreviews.app.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    CommandLineRunner commandLineRunner(ProductRepository productRepository, AccountRepository accountRepository) {
        return args ->  {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            Account admin = Account.createAccount(accountRepository, "username", passwordEncoder.encode("password"));
            accountRepository.save(admin);
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
