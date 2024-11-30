package org.products.productreviews;

import org.products.productreviews.app.entities.Account;
import org.products.productreviews.app.entities.Product;
import org.products.productreviews.app.entities.Review;
import org.products.productreviews.app.repositories.AccountRepository;
import org.products.productreviews.app.repositories.ProductRepository;
import org.products.productreviews.app.repositories.ReviewRepository;
import org.products.productreviews.web.util.ProductCategory;
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

    /**
     * Runner that will create products for using the application.
     * Ideally this is done through a View and by the request of a user, this method exists to bridge the gap between
     * now and when this feature is finished.
     * @param productRepository Autowired product repository
     * @return CommandLineRunner
     */
    @Bean
    CommandLineRunner commandLineRunner(ProductRepository productRepository, AccountRepository accountRepository, ReviewRepository reviewRepository) {
        return args ->  {
            //account for testing
            Account admin = Account.createAccount(accountRepository, "username", "MyPassword1!");
            accountRepository.save(admin);

            Product product1 = Product.createProduct(productRepository,
                    "E-Book Reader",
                    199.99f,
                    "A tablet used to purchase and read e-books",
                    null, ProductCategory.ELECTRONICS);
            Product product2 = Product.createProduct(productRepository,
                    "Lawnmower",
                    599.99f,
                    "A lawnmower to cut grass.",
                    null, ProductCategory.LANDSCAPING);
            Product product3 = Product.createProduct(productRepository,
                    "War and Peace",
                    19.99f,
                    "A long book, it has many pages.",
                    null, ProductCategory.BOOKS);
            Product product4 = Product.createProduct(productRepository,
                    "War and Peace 2",
                    19.99f,
                    "A long book, it has many more pages.",
                    null, ProductCategory.BOOKS);
            Product product5 = Product.createProduct(productRepository,
                    "Fountain Pen",
                    3.99f,
                    "This fountain pen is a well-crafted writing tool designed for anyone who enjoys a " +
                            "smooth and consistent writing experience. Its body is made from durable black resin with " +
                            "a polished finish, giving it a clean, professional look. The pen is accented with subtle " +
                            "silver trim, adding a touch of style without being too flashy. " +
                            "The stainless steel nib is designed for everyday writing and is available in fine, " +
                            "medium, or broad sizes to suit different writing preferences. It glides easily across " +
                            "the page, making it a great choice for both casual notes and longer writing sessions. " +
                            "The pen uses a piston-filling system, which allows you to fill it with ink from a " +
                            "bottle. This not only gives you a larger ink capacity but also lets you choose from a " +
                            "wide variety of ink colors. An ink window near the grip lets you see how much ink is " +
                            "left. The cap screws on securely to protect the nib and prevent leaks, and the " +
                            "sturdy clip makes it easy to carry in a pocket or notebook. " +
                            "Whether you're a student, professional, or just someone who enjoys writing by hand, " +
                            "this fountain pen is a reliable and stylish choice.",
                    null, ProductCategory.OFFICE_SUPPLIES);
            productRepository.save(product1);
            productRepository.save(product2);
            productRepository.save(product3);
            productRepository.save(product4);
            productRepository.save(product5);


            // Create Accounts
            Account account1 = Account.createAccount(accountRepository,
                    "user1",
                    "Pass1234!");
            accountRepository.save(account1);
            Account account2 = Account.createAccount(accountRepository,
                    "user2",
                    "Pass1234!");
            accountRepository.save(account2);

            // Extra sample users
            Account account3 = Account.createAccount(accountRepository,
                    "user3",
                    "Pass1234!");
            accountRepository.save(account3);
            Account account4 = Account.createAccount(accountRepository,
                    "user4",
                    "Pass1234!");
            accountRepository.save(account4);
            Account account5 = Account.createAccount(accountRepository,
                    "user5",
                    "Pass1234!");
            accountRepository.save(account5);

        };
    }

}
