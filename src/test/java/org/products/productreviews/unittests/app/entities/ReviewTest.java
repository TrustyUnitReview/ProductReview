package org.products.productreviews.unittests.app.entities;

import org.junit.jupiter.api.Test;
import org.products.productreviews.app.entities.Review;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReviewTest {

    /**
     * Tests Creating a Review
     */
    @Test
    void testBasicReviewCreation() {
        Review review = new Review(null, "Empty", Review.Star.ONE);
        assertNull(review.getAccount());
        assertEquals("Empty", review.getBody());
        assertEquals(Review.Star.ONE, review.getRating());
    }

    /**
     * Tests creating Review.Stars from Integers
     */
    @Test
    void testReviewStarCreation() {
        Review.Star starOne = Review.Star.fromInt(1);
        assertEquals(Review.Star.ONE, starOne);

        Review.Star starTwo = Review.Star.fromInt(2);
        assertEquals(Review.Star.TWO, starTwo);

        Review.Star starThree = Review.Star.fromInt(3);
        assertEquals(Review.Star.THREE, starThree);

        Review.Star starFour = Review.Star.fromInt(4);
        assertEquals(Review.Star.FOUR, starFour);

        Review.Star starFive = Review.Star.fromInt(5);
        assertEquals(Review.Star.FIVE, starFive);

        Review.Star starSix = Review.Star.fromInt(6);
        assertNull(starSix);

        Review.Star starNOne = Review.Star.fromInt(-1);
        assertNull(starNOne);

    }

}
