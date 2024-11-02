package org.products.productreviews.app.Repositories;

import org.products.productreviews.app.Entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
    boolean existsByUsername(String username);
}
