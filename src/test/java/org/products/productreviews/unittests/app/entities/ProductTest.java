package org.products.productreviews.unittests.app.entities;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.products.productreviews.app.entities.Product;
import org.products.productreviews.app.repositories.ProductRepository;
import org.products.productreviews.web.util.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.InvalidKeyException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductTest {
    @Autowired
    private ProductRepository repo;

    /**
     * Setup expected database for following tests.
     * @throws Exception if the factory creation fails (it shouldn't in theory)
     */
    @BeforeEach
    void setUp() throws Exception {
        Product validProduct = Product.createProduct(repo,"prodName1", 100f,"desc1", "img.png", ProductCategory.OFFICE_SUPPLIES);
        repo.save(validProduct);
    }

    /**
     * Drop table to set it up again.
     */
    @AfterEach
    void tearDown(){
        repo.deleteAll();
    }

    /**
     * We want to test a valid product, no exception is thrown.
     *
     * If an exception is thrown, test will fail!
     */
    @Test
    void createProductValid() throws Exception {
        // Test valid user creation cases
        Product valProduct = Product.createProduct(repo, "newProd", 100f, "desc2", "img.png", ProductCategory.OFFICE_SUPPLIES);
    }

    /**
     * We want to test an invalid product name (one which already exists).
     * If the correct exception is thrown (InvalidKeyException), test passes.
     * Other exceptions fail it.
     */
    @Test
    void createProductRepeatName() throws InvalidFormatException{
        // Checks the expected exception is thrown
        assertThrows(InvalidKeyException.class, () ->
                Product.createProduct(repo, "prodName1", 100f, "_", "_", ProductCategory.OFFICE_SUPPLIES)
        );
    }

    /**
     * We want to test the scoring system of reviews for a product.
     *
     * Currently, stubbed! Not much to do yet.
     */
    @Test
    void productScore() throws InvalidFormatException{
        Product testProduct = repo.findByName("prodName1");
        // Stubbing test value set to 0.0f for now
        assertEquals(0.0f, testProduct.getAvgReviewScore());
    }

    // Note: No tests for invalidFormats because those methods are not defined yet.
}