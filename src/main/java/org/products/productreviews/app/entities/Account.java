package org.products.productreviews.app.entities;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.persistence.*;
import org.products.productreviews.app.repositories.AccountRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import javax.management.openmbean.InvalidKeyException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;


@Entity
public class Account {
    @Id
    @Column(name="user_username", unique = true)
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
     * (which is feasible)
     * @param username The username to check for format validity.
     * @throws IllegalArgumentException If the username provided is in an invalid format
     */
    private static void checkUsernameFormat(String username) throws IllegalArgumentException {
        if (username.length() < 3 || username.length() > 20) {
            throw new IllegalArgumentException("Username must be between 3 and 20 characters long");
        }
        if (!username.matches("^[a-zA-Z0-9]*$")) {
            throw new IllegalArgumentException("Username can only contain alphanumeric characters");
        }
        if (username.contains(" ")) {
            throw new IllegalArgumentException("Username cannot contain spaces");
        }
    }

    /**
     * Format checks for password, throws error with rationale if a format check failed
     * @param password The password to check for format validity.
     * @throws IllegalArgumentException If the password provided is in an invalid format
     */
    private static void checkPasswordFormat(String password) throws IllegalArgumentException {
        if (password.length() < 8 || password.length() > 20) {
            throw new IllegalArgumentException("Password must be between 8 and 20 characters long");
        }
        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must contain at least one digit");
        }
        if (!password.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("Password must contain at least one lowercase letter");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Password must contain at least one uppercase letter");
        }
        if (!password.matches(".*[\\W_].*")) {
            throw new IllegalArgumentException("Password must contain at least one special character");
        }
    }

    /**
     * Input sanitation for user creation and encoding of password.
     * @param repo The user repository, instantiated through DI.
     * @param username A string username, should be (input sanitation rules here)
     * @param password A string password to match against for authentication.
     * @throws InvalidKeyException If the username provided already exists.
     * @throws IllegalArgumentException If the username or password provided is in an invalid format.
     * @return The new user created.
     */
    public static Account createAccount(AccountRepository repo, String username, String password) throws InvalidKeyException, InvalidFormatException {

        if (repo.existsByUsername(username)){
            throw new InvalidKeyException("Username already exists");
        }
        //throws IllegalArgumentException if username or password is invalid
        checkUsernameFormat(username);
        checkPasswordFormat(password);
        //if all checks pass, encode the password and create new Account
        var passwordEncoder = new BCryptPasswordEncoder();
        password = passwordEncoder.encode(password);

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
     * @return Reviews made by this user sorted by best to worst reviews
     */
    public Set<Review> getSortedReviews() {
        //Needed currently so reviews are displayed in the same order each reload - can double for filter
        return reviews.stream().
                sorted(Comparator.comparing(Review::getRating).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * Adds the provided Review to this account.
     * @param review The Review to be added
     * @return True if the Review was added, False if not.
     */
    public boolean addReview(Review review) {return reviews.add(review);}

    /**
     * Removes a review from user
     * @param review which is being deleted
     * @return True if review was removed, false otherwise.
     */
    public boolean removeReview(Review review) {return reviews.remove(review);}

    /**
     * @return Username of this account
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return Set of users followed
     */
    public Set<Account> getFollows() {return follows;}

    /**
     * Adds a user to follow set
     * @param account which is being followed
     */
    public void addFollows(Account account) {follows.add(account);}

    /**
     * Removes a user from the follow set
     * @param account which is being unfollowed
     */
    public void removeFollows(Account account) {follows.remove(account);}

}
