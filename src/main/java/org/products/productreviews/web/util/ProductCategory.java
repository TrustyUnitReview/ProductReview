package org.products.productreviews.web.util;

public enum ProductCategory {
    OFFICE_SUPPLIES("Office Supplies"),
    ELECTRONICS("Electronics"),
    LANDSCAPING("Landscaping"),
    BOOKS("Books");

    private final String displayName;

    ProductCategory(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "Product category: " + displayName;
    }
}
