package org.products.productreviews.app.entities;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.persistence.*;
import org.products.productreviews.app.repositories.ProductRepository;
import org.products.productreviews.web.util.ProductCategory;

import java.awt.*;
import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Product {
    @Id
    @GeneratedValue
    private Long id;
    // Should this be the PK?
    @Column(unique=true)
    private String name;
    private float price;
    @Column(columnDefinition = "LONGTEXT")
    private String description;
    // Cannot store Image or Path objects directly, required to store string to load later
    private String imagePath;
    @Transient
    private Image loadedImage;
    @ElementCollection
    private Set<String> sellerLinks;
    // Make sure no review exists without a product
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "product")
    private Set<Review> reviews;
    @Column
    public ProductCategory category;

    /**
     * Protected constructor used by JPA for reflexive construction.
     */
    protected Product(){
        loadImage();
    }

    /**
     * Private constructor used by factory. See factory method for more details.
     *
     * @param name The name of the product.
     * @param description The description of the product.
     * @param imagePath The filepath given as a String.
     */
    private Product(String name, float price, String description, String imagePath, ProductCategory category) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imagePath = imagePath;
        this.reviews = new HashSet<>();
        this.category = category;
        loadImage(); // Cannot re-use protected constructor, has to be called after imagePath is set.
    }

    /**
     * Input sanitation for product creation, allows for creation failure with exception throws.
     * This factory is currently the only intended way to create Products.
     *
     * @param repo The ProductRepository reference.
     * @param name The name of the product.
     * @param description The description of the product.
     * @param imagePath The filepath given as a String.
     *
     * @return A new product object.
     *
     * @throws InvalidKeyException If the product name already exists (must be unique).
     * @throws InvalidFormatException If a description, or imagePath does not follow a proper format
     *  (see "check..." helper methods for format specifications).
     */
    public static Product createProduct(ProductRepository repo, String name, float price, String description, String imagePath, ProductCategory category)
            throws InvalidKeyException, InvalidFormatException {
        if (repo.existsByName(name)){
            throw new InvalidKeyException("Name already exists");
        }
        checkDescriptionFormat();
        checkImagePath();

        return new Product(name, price, description, imagePath, category);
    }

    private static void checkDescriptionFormat() throws InvalidFormatException {
    }

    private static void checkImagePath() throws InvalidFormatException {
        // TODO: Implement this, throw error on fail
    }

    private void loadImage() {
        // TODO: Implement this, set loadedImage using imagePath
    }

    /**
     * Calculate real average review score of the product.
     * @return float average review score of the product
     */
    public float getAvgReviewScore(){
        if(reviews == null || reviews.isEmpty()){
            return 0.0f;
        }
        float sum = 0.0f;
        for(Review review : reviews){
            sum += review.getRating().getValue();
        }

        return sum / reviews.size();
    }

    /**
     * Calculate average review score of the product and round it to 1 decimal place.
     * For view purposes.
     * @return
     */
    public double getRoundedAvgReviewScore(){
        return Math.round(getAvgReviewScore() * 10.0) / 10.0;
    }

    /**
     *
     * @return The name of the product
     */
    public String getName(){ //required for web testing
        return name;
    }

    /**
     * @return The description of the product.
     */
    public String getDescription(){ //required for web testing
        return description;
    }

    /**
     *
     * @return The price of the product.
     */
    public float getPrice(){ //required for web testing
        return price;
    }

    /**
     * @return The image representing the product.
     */
    public Image getImage(){
        return loadedImage;
    }

    /**
     * @return reviews of the product.
     */
    public Set<Review> getReviews(){
        return reviews;
    }

    /**
     *
     * @param review
     */
    public void addReview(Review review){
        reviews.add(review);
    }

    /**
     * Get category of this product
     * @return the category of this product
     */
    public ProductCategory getCategory(){
        return category;
    }

    /**
     * Get the categories of this product
     * @return the category of this product
     */
    public String getCategoryString(){
        return category.displayOnDashboard();
    }

    /**
     * Sets the category of this item
     * @param category
     */
    public void setCategory(ProductCategory category){
        this.category = category;
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
        if (obj instanceof Product product){
            return this.name.equals(product.name);
        } return false;
    }
}
