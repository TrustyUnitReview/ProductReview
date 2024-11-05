package org.products.productreviews.app.entities;

import org.products.productreviews.app.UserRole;

public class AdminAccount extends Account {
    private UserRole role = UserRole.ADMIN;

    public AdminAccount() {
    }
}
