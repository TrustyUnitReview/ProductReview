package org.products.productreviews.app.repositories;

import org.products.productreviews.app.entities.Product;
import org.products.productreviews.web.util.ProductCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {
    boolean existsByName(String name);
    Product findByName(String name);

    // Filter products by category
    List<Product> findByCategory(ProductCategory category);
}
