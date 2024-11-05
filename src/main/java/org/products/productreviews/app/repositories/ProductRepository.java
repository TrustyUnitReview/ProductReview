package org.products.productreviews.app.repositories;

import org.products.productreviews.app.entities.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
    boolean existsByName(String name);
}
