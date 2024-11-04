package org.products.productreviews.app.entities;

import org.products.productreviews.app.UserRole;

public class AdminUser extends User {
    private UserRole role = UserRole.ADMIN;

    public AdminUser() {
    }
}
