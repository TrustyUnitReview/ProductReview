package org.products.productreviews.web.request;

import org.products.productreviews.app.entities.Account;
import org.products.productreviews.app.entities.Review;

/**
 * This class serves as a way to gather data for review creation.
 * Allows for malformed requests to be detected before an error occurs
 * and serves as an object to be passed to facilitate Review creation in a forum.
 */
public class ReviewRequest {

    private String body;
    private String rating;

    /**
     * Creates a {@link Review.Star} from a string value which is either numeric or the number spelt out.
     * <br> For example the value could be: "5" or "FIVE"
     * @return A {@link Review.Star} representing the given rating.
     */
    private Review.Star parseRating(){
        if (rating.chars().allMatch( Character::isDigit )){
            return Review.Star.fromInt(Integer.parseInt(rating));
        } else {
            return Review.Star.valueOf(rating);
        }
    }
    
    public ReviewRequest(){}

    /**
     * Creates a Review from this ReviewRequest.
     * Does not fill in the associated Product.
     * <br>
     * <ul>
     *      <li>Review's product field is assigned by adding it to a Product and saving the Product</li>
     *      <li>Account needs to be added to Review</li>
     *      <li>Review will be saved if the Product it was added to is Saved</li>
     * </ul>
     * @param account The Current Account
     * @return The Created Review
     */
    public Review toReview(Account account) {
        Review review = new Review();
        review.setAccount(account);
        review.setBody(body);
        review.setRating(parseRating());
        return review;
    }

    /**
     * @return Review Body
     */
    public String getBody() {return body;}

    /**
     * @param body Review Body to be set
     */
    public void setBody(String body) {this.body = body;}

    /**
     * @return Rating as a String
     */
    public String getRating() {return rating;}

    /**
     * @param rating Rating as a String
     */
    public void setRating(String rating) {this.rating = rating;}

}
