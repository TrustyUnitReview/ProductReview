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
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam efficitur placerat " +
                                "magna quis porta. Praesent eget urna ac felis egestas faucibus. Suspendisse dolor enim," +
                                " fringilla vitae erat in, auctor suscipit nibh. Nunc sed felis finibus, fringilla dui " +
                                "sagittis, pharetra augue. Fusce vel nulla varius, sagittis nibh ac, porta erat. Integer" +
                                " arcu leo, aliquet eu turpis eget, semper convallis leo. Praesent iaculis sed diam in " +
                                "rutrum. Curabitur blandit dui odio, sed eleifend neque laoreet eu. Pellentesque " +
                                "habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. " +
                                "Curabitur hendrerit est tortor. Nullam porta lorem felis, et consectetur nisi placerat " +
                                "sit amet. Proin sapien est, aliquet rutrum neque quis, viverra placerat metus. Proin " +
                                "lacinia lectus pretium blandit egestas Quisque vel sapien varius, rutrum odio nec, " +
                                "lobortis tellus. Duis malesuada fringilla nulla eu ultricies. Proin eu malesuada est. " +
                                "Etiam fermentum, nulla nec ornare iaculis, purus ante egestas nibh, id rhoncus ipsum " +
                                "augue nec enim. Aliquam purus ligula, faucibus quis orci non, laoreet efficitur quam. " +
                                "Integer rhoncus purus quam, at laoreet magna interdum consectetur. Nullam in metus vel " +
                                "magna facilisis pulvinar ac eu orci. Donec et pulvinar orci, a ullamcorper lectus. " +
                                "Sed a ex mauris. Curabitur ut felis urna. Praesent blandit faucibus quam Donec sit " +
                                "amet hendrerit mauris. In congue varius nisi, sed posuere lectus iaculis in. Mauris " +
                                "et purus lacinia, interdum magna eu, volutpat arcu. Praesent malesuada ullamcorper " +
                                "scelerisque. Curabitur accumsan eros et ex iaculis pellentesque. Nam id nisi nec orci " +
                                "porta volutpat. In varius, turpis sed ultricies blandit, magna augue pretium metus, " +
                                "at eleifend sapien massa ac velit. Vestibulum bibendum eros erat, sit amet lobortis " +
                                "dui venenatis sed Donec tempor aliquam justo, in aliquam urna ultrices nec. Interdum " +
                                "et malesuada fames ac ante ipsum primis in faucibus. Sed at aliquam ante. Sed nec " +
                                "odio eget nunc ornare dapibus ornare a magna. Quisque vitae felis vel lacus facilisis " +
                                "hendrerit. Vestibulum quis augue aliquam, gravida tortor sit amet, fringilla sem. Sed " +
                                "eget mi neque. Nunc porttitor justo sit amet nulla feugiat, vitae auctor nisi " +
                                "interdum. Mauris ac tellus ac leo pretium blandit efficitur sed mauris. Nunc " +
                                "consequat est eu semper dictum. Curabitur consequat, ante at hendrerit pulvinar, " +
                                "magna dui consequat dolor, vel egestas urna tellus eget nunc. Nullam eget neque quis " +
                                "ligula dignissim gravida ut at sapien. Orci varius natoque penatibus et magnis dis " +
                                "parturient montes, nascetur ridiculus mus  In luctus neque ac fringilla eleifend. Sed " +
                                "ante tortor, mattis vel nulla et, consequat bibendum nisl. Nunc augue neque, placerat " +
                                "vitae accumsan sed, sollicitudin sed sem. Aliquam sit amet magna enim. In tellus mi, " +
                                "tincidunt vel consectetur quis, semper vel mauris. Mauris a quam vel lectus tristique " +
                                "eleifend a nec nisi. Nam non porta sem. Fusce sit amet maximus eros. Sed eget tellus " +
                                "lectus. Sed luctus sapien ac vestibulum viverra. ",
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
