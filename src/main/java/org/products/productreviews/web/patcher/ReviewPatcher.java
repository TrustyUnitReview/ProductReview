package org.products.productreviews.web.patcher;

import org.products.productreviews.app.Review;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class ReviewPatcher {

    public static void reviewPatch(Review existing, Review incomplete) throws IllegalAccessException {

        // Gain direct access to class fields and edit them. Use "incomplete" to edit existing.
        Class<?> reviewClass = Review.class;
        Field[] reviewFields = reviewClass.getDeclaredFields();

        for (Field field : reviewFields) {
            field.setAccessible(true);
            Object value = field.get(incomplete);
            if (value != null) {
                field.set(existing, value);
            }
            field.setAccessible(false);
        }

    }

}
