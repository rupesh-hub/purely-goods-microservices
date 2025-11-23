package com.alfarays.model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
@Schema(
        name = "CategoryRequest",
        description = "Request payload for creating or updating a product category."
)
public class CategoryRequest {

    @NotBlank(message = "Category name is required")
    @Schema(
            description = "Name of the category",
            example = "Electronics",
            required = true
    )
    private String name;
}