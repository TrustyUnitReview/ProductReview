package org.products.productreviews.app;

public class AdminUser {
    private User dummyUser;

    private String userName = "ADMINUSER";
    private String password = "password123";
    private UserRole role = UserRole.ADMIN;

    public AdminUser() {
        dummyUser = new User();
        dummyUser.setUserName(userName);
        dummyUser.setPassword(password);
        dummyUser.setRole(role);
    }
}
