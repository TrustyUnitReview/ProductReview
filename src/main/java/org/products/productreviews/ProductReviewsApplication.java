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
            account2.addFollows(account1);
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

            //following
            account2.addFollows(account1);

            createReviews(product1, product2, product3, product4, account1, account2, account3, account4, account5, reviewRepository);
        };
    }

    /**
     * Hard code reviews for products. Mostly made for testing purposes.
     */
    private void createReviews(Product p1, Product p2, Product p3, Product p4,
                               Account a1, Account a2, Account a3, Account a4, Account a5, ReviewRepository reviewRepository) {

        //create Reviews and assign them to accounts
        Review r1 = new Review(a1, "Best e-reader out there! I never have to worry about not finding a book I want to read.",
                            Review.Star.FIVE);
        a1.addReview(r1);
        Review r2 = new Review(a2, "I really liked this device! However I wish it came in different colours",
                            Review.Star.FOUR);
        a2.addReview(r2);
        Review r3 = new Review(a3, "Very nice lawnmower, it has lasted me for years!",
                            Review.Star.FIVE);
        a3.addReview(r3);
        Review r4 = new Review(a4, "I've been buying lawnmowers from this company for years. They last a long time and get the job done quickly!",
                            Review.Star.FIVE);
        a4.addReview(r4);
        Review r5 = new Review(a1, "Very dry read, I don't understand the hype of Tolstoy.",
                            Review.Star.ONE);
        a1.addReview(r5);
        Review r6 = new Review(a5, "Book sounded too scripted, as if the author was rushing. It was a good story but could have been written much better.",
                            Review.Star.TWO);
        a5.addReview(r6);
        Review r7 = new Review(a3, "This book was even better than the first! I finished it in a day because of how captivating it was. Highly recommend!",
                            Review.Star.FIVE);
        a3.addReview(r7);
        Review r8 = new Review(a2, "Overall this was a good story but do think that the ending could have been better...",
                Review.Star.THREE);
        a2.addReview(r8);

        //assign reviews to products and products to reviews
        p1.addReview(r1);
        r1.setProduct(p1);
        p1.addReview(r2);
        r2.setProduct(p1);

        p2.addReview(r3);
        r3.setProduct(p2);
        p2.addReview(r4);
        r4.setProduct(p2);

        p3.addReview(r5);
        r5.setProduct(p3);
        p3.addReview(r6);
        r6.setProduct(p3);

        p4.addReview(r7);
        r7.setProduct(p4);
        p4.addReview(r8);
        r8.setProduct(p4);

        reviewRepository.save(r1);
        reviewRepository.save(r2);
        reviewRepository.save(r3);
        reviewRepository.save(r4);
        reviewRepository.save(r5);
        reviewRepository.save(r6);
        reviewRepository.save(r7);
        reviewRepository.save(r8);

    }

}
