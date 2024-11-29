package org.products.productreviews.unittests.app.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.products.productreviews.app.entities.Product;
import org.products.productreviews.app.repositories.ProductRepository;
import org.products.productreviews.web.util.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductRepositoryTest {
    @Autowired
    private ProductRepository repo;

    /**
     * Setup expected database for following tests.
     * @throws Exception if the factory creation fails (it shouldn't in theory)
     */
    @BeforeEach
    void setUp() throws Exception {
        Product validAccount = Product.createProduct(repo,"test1", 100f,"d1", "_.png", ProductCategory.OFFICE_SUPPLIES);
        repo.save(validAccount);
    }

    /**
     * Drop table to set it up again.
     */
    @AfterEach
    void tearDown(){
        repo.deleteAll();
    }

    /**
     * This test checks if the existsByName works as intended.
     * That is, returns true if the name exists in the DB, and false otherwise.
     */
    @Test
    void existsByName() {
        assertTrue(repo.existsByName("test1"));
        assertFalse(repo.existsByName("userNotInDB"));
    }

    /**
     * This test checks if the findByName works as intended.
     * Checks that a product is returned, check as many known properties as possible.
     */
    @Test
    void findByName() {
        Product testProduct = repo.findByName("test1");

        assertNotNull(testProduct);
        assertEquals(0.0f, testProduct.getAvgReviewScore());
    }
}