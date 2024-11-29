package org.products.productreviews.web.util;

/**
 * Enum representing the category of a product
 */
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
        return displayName;
    }

    /**
     * Display the category in readable form
     * Used on the dashboard
     * @return The category name
     */
    public String displayOnDashboard() {
        return "Product category: " + displayName;
    }

    /**
     * Get the category from the display name
     * @param displayName The display name of the category
     * @return The category
     */
    public static ProductCategory fromDisplayName(String displayName) {
        for (ProductCategory category : ProductCategory.values()) {
            if (category.toString().equalsIgnoreCase(displayName)) {
                return category;
            }
        }
        throw new IllegalArgumentException("No enum constant with display name " + displayName);
    }
}
