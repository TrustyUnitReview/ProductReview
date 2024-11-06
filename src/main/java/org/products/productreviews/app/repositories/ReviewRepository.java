package org.products.productreviews.app.repositories;

import org.products.productreviews.app.entities.Review;
import org.springframework.data.repository.CrudRepository;


public interface ReviewRepository extends CrudRepository<Review, Long> {
}
