package com.alfarays.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * DTO for product creation and update requests
 * Handles MultipartFile images instead of pre-uploaded image IDs
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        name = "ProductRequest",
        description = "Request payload for creating or updating a product. Includes category, specifications, and image uploads."
)
public class ProductRequest {

    @NotBlank(message = "Product name is required")
    @Schema(
            description = "Name of the product",
            example = "Samsung Galaxy S24 Ultra"
    )
    private String name;

    @NotNull(message = "Category ID is required")
    @Positive(message = "Category ID must be a positive number")
    @Schema(
            description = "ID of the category to which the product belongs",
            example = "3"
    )
    private Long categoryId;

    @Schema(
            description = "Product specifications in key-value format",
            example = "{\"color\": \"black\", \"storage\": \"256GB\"}",
            type = "object"
    )
    private Map<String, String> specification;

    @Schema(
            description = "List of product images uploaded using multipart/form-data",
            type = "array",
            format = "binary"
    )
    private MultipartFile[] images;
}
