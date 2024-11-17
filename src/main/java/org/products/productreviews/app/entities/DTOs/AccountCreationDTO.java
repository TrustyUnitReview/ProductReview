package org.products.productreviews.app.entities.DTOs;

import jakarta.validation.constraints.*;

public class AccountCreationDTO {

    @NotEmpty
    @Size(min = 3, max = 20, message = "Username must be >3 characters long")
    private String username;

    @NotEmpty
    @Size(min = 8, message = "Minimum Password length is 8 characters")
    private String password;

    private String confirmPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
