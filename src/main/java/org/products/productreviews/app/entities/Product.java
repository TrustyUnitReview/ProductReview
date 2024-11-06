package org.products.productreviews.app.entities;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.persistence.*;
import org.products.productreviews.app.repositories.ProductRepository;

import java.awt.*;
import java.security.InvalidKeyException;
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
    private String description;
    // Cannot store Image or Path objects directly, required to store string to load later
    private String imagePath;
    @Transient
    private Image loadedImage;
    @ElementCollection
    private Set<String> sellerLinks;
    // FIXME: Requires review to be an entity.
    @OneToMany
    private Set<Review> reviews;

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
    private Product(String name, float price, String description, String imagePath){
        this.name = name;
        this.price = price;
        this.description = description;
        this.imagePath = imagePath;
        this.reviews = new HashSet<>();
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
    public static Product createProduct(ProductRepository repo, String name, float price, String description, String imagePath)
            throws InvalidKeyException, InvalidFormatException {
        if (repo.existsByName(name)){
            throw new InvalidKeyException("Name already exists");
        }
        checkDescriptionFormat();
        checkImagePath();

        return new Product(name, price, description, imagePath);
    }

    private static void checkDescriptionFormat() throws InvalidFormatException {
    }

    private static void checkImagePath() throws InvalidFormatException {
        // TODO: Implement this, throw error on fail
    }

    private void loadImage() {
        // TODO: Implement this, set loadedImage using imagePath
    }

    public float getReviewScore(){
        // TODO: Implement scoring algorithm... May take in a strategy later
        return 0.0f; // Between 1-5
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
}
