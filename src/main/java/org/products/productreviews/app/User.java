package org.products.productreviews.app;

import java.util.ArrayList;

public class User {

    private String userName;
    private String password;
    private UserRole role;

    private ArrayList<Review> reviews;

    public ArrayList<Review> getReviews() {
        return reviews;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
