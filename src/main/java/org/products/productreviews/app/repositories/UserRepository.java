package org.products.productreviews.app.repositories;

import org.products.productreviews.app.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
    boolean existsByUsername(String username);
}
