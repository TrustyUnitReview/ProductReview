package org.products.productreviews.app.entities;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.persistence.*;
import org.products.productreviews.app.repositories.AccountRepository;
import org.products.productreviews.app.util.AccountSeparation;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import javax.management.openmbean.InvalidKeyException;
import java.util.*;
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
    private int followCount;

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
     * @return Number of users following this user
     */
    public int getFollowerCount() {return followCount;}


    /**
     * @return Set of users followed
     */
    public Set<Account> getFollows() {return follows;}

    /**
     * Adds a user to follow set
     * @param account which is being followed
     */
    public void addFollows(Account account) {
        account.followCount++;
        follows.add(account);
    }

    /**
     * Removes a user from the follow set
     * @param account which is being unfollowed
     */
    public void removeFollows(Account account) {
        account.followCount--;
        follows.remove(account);
    }

    /**
     * Retrieves the separation between this account and a chosen account
     * @param account The account to reference separation from
     * @return integer representing degree of separation
     */
    public String getSeparationStr(Account account) {
        return switch (AccountSeparation.getSeparation(account, this)) {
            case 0 -> "No separation";
            case 1 -> "1st Degree";
            case 2 -> "2nd Degree";
            case 3 -> "3rd Degree+";
            default -> "N/A";
        };
    }


    /**
     * Indicates whether some other object is "equal to" this one.
     * <p>
     * The {@code equals} method implements an equivalence relation
     * on non-null object references:
     * <ul>
     * <li>It is <i>reflexive</i>: for any non-null reference value
     *     {@code x}, {@code x.equals(x)} should return
     *     {@code true}.
     * <li>It is <i>symmetric</i>: for any non-null reference values
     *     {@code x} and {@code y}, {@code x.equals(y)}
     *     should return {@code true} if and only if
     *     {@code y.equals(x)} returns {@code true}.
     * <li>It is <i>transitive</i>: for any non-null reference values
     *     {@code x}, {@code y}, and {@code z}, if
     *     {@code x.equals(y)} returns {@code true} and
     *     {@code y.equals(z)} returns {@code true}, then
     *     {@code x.equals(z)} should return {@code true}.
     * <li>It is <i>consistent</i>: for any non-null reference values
     *     {@code x} and {@code y}, multiple invocations of
     *     {@code x.equals(y)} consistently return {@code true}
     *     or consistently return {@code false}, provided no
     *     information used in {@code equals} comparisons on the
     *     objects is modified.
     * <li>For any non-null reference value {@code x},
     *     {@code x.equals(null)} should return {@code false}.
     * </ul>
     *
     * <p>
     * An equivalence relation partitions the elements it operates on
     * into <i>equivalence classes</i>; all the members of an
     * equivalence class are equal to each other. Members of an
     * equivalence class are substitutable for each other, at least
     * for some purposes.
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     * argument; {@code false} otherwise.
     * @implSpec The {@code equals} method for class {@code Object} implements
     * the most discriminating possible equivalence relation on objects;
     * that is, for any non-null reference values {@code x} and
     * {@code y}, this method returns {@code true} if and only
     * if {@code x} and {@code y} refer to the same object
     * ({@code x == y} has the value {@code true}).
     * <p>
     * In other words, under the reference equality equivalence
     * relation, each equivalence class only has a single element.
     * @apiNote It is generally necessary to override the {@link #hashCode() hashCode}
     * method whenever this method is overridden, so as to maintain the
     * general contract for the {@code hashCode} method, which states
     * that equal objects must have equal hash codes.
     * @see #hashCode()
     * @see HashMap
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Account account){
            return this.username.equals(account.username);
        } return false;
    }
}
