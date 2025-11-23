package com.alfarays.resource;

import com.alfarays.model.CategoryRequest;
import com.alfarays.model.CategoryResponse;
import com.alfarays.model.GlobalResponse;
import com.alfarays.service.ICategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
@Tag(
        name = "Category API",
        description = "CRUD operations for product categories"
)
public class CategoryResource {

    private final ICategoryService categoryService;

    // CREATE --------------------------------------------------------------
    @PostMapping
    @Operation(
            summary = "Create a new category",
            description = "Creates a product category and returns the created resource.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Category created successfully",
                            content = @Content(schema = @Schema(implementation = CategoryResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request payload",
                            content = @Content(schema = @Schema(implementation = GlobalResponse.class))
                    )
            }
    )
    public ResponseEntity<GlobalResponse<CategoryResponse>> create(
            @Valid @RequestBody CategoryRequest request) {

        log.info("Creating category: {}", request.getName());

        CategoryResponse response = categoryService.create(request);

        return ResponseEntity
                .status(201)
                .body(GlobalResponse.success("Category created", response));
    }

    // UPDATE --------------------------------------------------------------
    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing category",
            description = "Updates category details by ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Category updated"),
                    @ApiResponse(responseCode = "404", description = "Category not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            }
    )
    public ResponseEntity<GlobalResponse<CategoryResponse>> update(
            @Parameter(description = "Category ID", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {

        log.info("Updating category with id: {}", id);

        CategoryResponse response = categoryService.update(id, request);

        return ResponseEntity.ok(
                GlobalResponse.success("Category updated", response)
        );
    }

    // GET BY ID -----------------------------------------------------------
    @GetMapping("/{id}")
    @Operation(
            summary = "Get category by ID",
            description = "Fetches category details using category ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Category found"),
                    @ApiResponse(responseCode = "404", description = "Category not found")
            }
    )
    public ResponseEntity<GlobalResponse<CategoryResponse>> get(
            @Parameter(description = "Category ID", example = "1")
            @PathVariable Long id) {

        log.info("Fetching category with id: {}", id);

        CategoryResponse response = categoryService.get(id);

        return ResponseEntity.ok(
                GlobalResponse.success("Category fetched", response)
        );
    }

    // GET ALL -------------------------------------------------------------
    @GetMapping
    @Operation(
            summary = "Get all categories",
            description = "Returns list of all product categories."
    )
    public ResponseEntity<GlobalResponse<List<CategoryResponse>>> getAll() {

        log.info("Fetching all categories");

        List<CategoryResponse> response = categoryService.getAll();

        return ResponseEntity.ok(
                GlobalResponse.success("Categories fetched", response)
        );
    }

    // DELETE --------------------------------------------------------------
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete category",
            description = "Deletes a category by ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Category deleted"),
                    @ApiResponse(responseCode = "404", description = "Category not found")
            }
    )
    public ResponseEntity<Void> delete(
            @Parameter(description = "Category ID", example = "1")
            @PathVariable Long id) {

        log.info("Deleting category with id: {}", id);

        categoryService.delete(id);

        return ResponseEntity.noContent().build();
    }

}
