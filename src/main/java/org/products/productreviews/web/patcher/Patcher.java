package org.products.productreviews.web.patcher;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class Patcher {

    public static void patch(Object existing, Object incomplete) throws IllegalAccessException {
        // Gain direct access to class fields and edit them. Use "incomplete" to edit existing.
        Class<?> c = existing.getClass();
        Field[] classFields = c.getDeclaredFields();

        for (Field field : classFields) {
            field.setAccessible(true);
            Object value = field.get(incomplete);
            if (value != null) {
                field.set(existing, value);
            }
            field.setAccessible(false);
        }
    }
}
