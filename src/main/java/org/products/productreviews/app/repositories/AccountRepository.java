package org.products.productreviews.app.repositories;

import org.products.productreviews.app.entities.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, String> {
    boolean existsByUsername(String username);
    Account findByUsername(String username);
}
