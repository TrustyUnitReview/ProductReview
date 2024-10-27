package org.products.productreviews;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Logger;

@SpringBootApplication
public class ProductReviewsApplication {

    public static Logger LOGGER = Logger.getLogger("ProductReviewsApplication");


    public static void main(String[] args) {
        SpringApplication.run(ProductReviewsApplication.class, args);
    }

}
