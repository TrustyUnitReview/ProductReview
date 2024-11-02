package org.products.productreviews.app.Entities;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.products.productreviews.app.Repositories.UserRepository;

import javax.management.openmbean.InvalidKeyException;


@Entity
public class User {
    @Id
    private String username;
    // TODO: Replace this unsafe string store with a true authentication method
    private String password;
//    // TODO: JPA Annotations
//    private Set<User> follows;
//    // TODO: JPA Annotations, can we change to Set to ensure uniqueness?
//    private ArrayList<Review> reviews;

    /**
     * Private constructor for factory use.
     *
     * @param username Unique username, assumed through factory call.
     * @param password Authentication password.
     */
    private User(String username, String password) {
        this.username = username;
        this.password = password;
//        this.follows = new HashSet<>();
//        this.reviews = new ArrayList<>();
    }

    // For JPA reflection, should never be called in theory.
    protected User() {}

    /**
     * Format checks for username, throws error with rationale if a format check failed.
     * Hands control back to caller otherwise.
     * Limitation: Will only return the first error, unless we implement a string builder for the error message
     * (which is feasible).
     *
     * @throws InvalidFormatException If the username provided is in an invalid format
     */
    private static void checkUsernameFormat() throws InvalidFormatException {
        // TODO: Implement sanitation rules, throw exceptions for usernames outside of them.
    }

    /**
     * Input sanitation for user creation.
     * Factory should be main (exclusive?) way to create users.
     *
     * @param repo The user repository, instanciated through DI.
     * @param username A string username, should be (input sanitation rules here)
     * @param password A string password to match against for authentication.
     *
     * @throws InvalidKeyException If the username provided already exists.
     * @throws InvalidFormatException If the username provided is in an invalid format.
     *
     * @return The new user created.
     */
    public static User createUser(UserRepository repo, String username, String password) throws InvalidKeyException, InvalidFormatException {
        // Can we access the repo without it being a param? We seem to access it through DI atm.
        // I don't think so, but I might be missing something.
        if (repo.existsByUsername(username)){
            throw new InvalidKeyException("Username already exists");
        }
        // Will throw error and stop if format check fails
        checkUsernameFormat();

        // TODO: Allow for client side password strength requirements?
        //  Maybe auth strategy takes care of that for us?
        return new User(username, password);
    }

//    public ArrayList<Review> getReviews() {
//        return reviews;
//    }
}
