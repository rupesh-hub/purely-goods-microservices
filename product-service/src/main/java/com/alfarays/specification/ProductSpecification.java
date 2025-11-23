package com.alfarays.specification;

import com.alfarays.entity.Product;
import org.springframework.data.jpa.domain.Specification;

/**
 * JPA Specification for filtering products with various criteria
 */
public class ProductSpecification {

    private ProductSpecification() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Filter by product name (contains)
     */
    public static Specification<Product> byName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.trim().isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")),
                    "%" + name.toLowerCase().trim() + "%"
            );
        };
    }

    /**
     * Filter by category ID
     */
    public static Specification<Product> byCategory(Long categoryId) {
        return (root, query, criteriaBuilder) -> {
            if (categoryId == null) {
                return null;
            }
            return criteriaBuilder.equal(
                    root.get("category").get("id"),
                    categoryId
            );
        };
    }

    /**
     * Filter by category name
     */
    public static Specification<Product> byCategoryName(String categoryName) {
        return (root, query, criteriaBuilder) -> {
            if (categoryName == null || categoryName.trim().isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("category").get("name")),
                    "%" + categoryName.toLowerCase().trim() + "%"
            );
        };
    }

    /**
     * Filter by price range
     */
    public static Specification<Product> byPriceRange(Double minPrice, Double maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice == null && maxPrice == null) {
                return null;
            }
            if (minPrice != null && maxPrice != null) {
                return criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
            }
            if (minPrice != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
        };
    }

    /**
     * Combine multiple filters with proper null handling
     */
    public static Specification<Product> filter(String name, Long categoryId, String categoryName) {
        return Specification.allOf(
                byName(name),
                byCategory(categoryId),
                byCategoryName(categoryName)
        );
    }

    /**
     * Advanced filter with price range
     */
    public static Specification<Product> filterAdvanced(String name, Long categoryId, String categoryName, Double minPrice, Double maxPrice) {
        return Specification
                .where(byName(name))
                .and(byCategory(categoryId))
                .and(byCategoryName(categoryName))
                .and(byPriceRange(minPrice, maxPrice));
    }
}
