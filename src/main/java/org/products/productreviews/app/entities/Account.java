package org.products.productreviews.app.entities;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.persistence.*;
import org.products.productreviews.app.repositories.AccountRepository;

import javax.management.openmbean.InvalidKeyException;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Account {
    @Id
    @Column(name="user_username", unique = true) //TODO: idk if this unique key needs to be here
    private String username;
    private String password;

    @ManyToMany
    private Set<Account> follows;
    @OneToMany
    @JoinColumn(name="user_username")
    private Set<Review> reviews;

    /**
     * Private constructor for factory use.
     *
     * @param username Unique username, assumed through factory call.
     * @param password Authentication password.
     */
    private Account(String username, String password) {
        this.username = username;
        this.password = password;
        this.follows = new HashSet<>();
        this.reviews = new HashSet<>();
    }

    // For JPA reflection, should never be called in theory.
    protected Account() {}

    /**
     * Format checks for username, throws error with rationale if a format check failed.
     * Hands control back to caller otherwise.
     * Limitation: Will only return the first error, unless we implement a string builder for the error message
     * (which is feasible).
     *
     * @param username The username to check for format validity.
     *
     * @throws InvalidFormatException If the username provided is in an invalid format
     */
    private static void checkUsernameFormat(String username) throws InvalidFormatException {
        // TODO: Implement sanitation rules, throw exceptions for usernames outside of them.
    }

    /**
     * Input sanitation for user creation.
     * Factory should be main (exclusive?) way to create users.
     *
     * @param repo The user repository, instantiated through DI.
     * @param username A string username, should be (input sanitation rules here)
     * @param password A string password to match against for authentication.
     *
     * @throws InvalidKeyException If the username provided already exists.
     * @throws InvalidFormatException If the username or password provided is in an invalid format.
     *
     * @return The new user created.
     */
    public static Account createAccount(AccountRepository repo, String username, String password) throws InvalidKeyException, InvalidFormatException {
        // Can we access the repo without it being a param? We seem to access it through DI atm.
        // I don't think so, but I might be missing something.
        if (repo.existsByUsername(username)){
            throw new InvalidKeyException("Username already exists");
        }
        // Will throw error and stop if format check fails
        checkUsernameFormat(username); //TODO: what is the format check?



        return new Account(username, password);
    }

    /**
     * @return Password of this account
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return Reviews made by this user.
     */
    public Set<Review> getReviews() {
        return reviews;
    }

    /**
     * Adds the provided Review to this account.
     * @param review The Review to be added
     * @return True if the Review was added, False if not.
     */
    public boolean addReview(Review review) {return reviews.add(review);}

    /**
     * @return Username of this account
     */
    public String getUsername() {
        return username;
    }

}
