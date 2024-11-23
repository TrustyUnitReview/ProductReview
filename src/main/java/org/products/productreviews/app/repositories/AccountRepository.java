package org.products.productreviews.app.repositories;

import org.products.productreviews.app.entities.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, String> {
    boolean existsByUsername(String username);
    Optional<Account> findByUsername(String username);
}
