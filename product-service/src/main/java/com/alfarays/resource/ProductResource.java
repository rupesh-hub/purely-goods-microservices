package com.alfarays.resource;

import com.alfarays.model.ErrorResponse;
import com.alfarays.model.ProductRequest;
import com.alfarays.model.ProductResponse;
import com.alfarays.service.IProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(
        name = "Products",
        description = "REST endpoints for managing products, specifications, categories, and images"
)
public class ProductResource {

    private final IProductService productService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Create a new product",
            description = "Allows creation of a product along with specifications and multiple image uploads."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    description = "Product created successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),

            @ApiResponse(responseCode = "400",
                    description = "Invalid request payload or malformed specifications JSON",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),

            @ApiResponse(responseCode = "500",
                    description = "Unexpected internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ProductResponse> create(
            @Parameter(description = "Product name", required = true)
            @RequestParam String name,

            @Parameter(description = "Category ID", example = "1", required = true)
            @RequestParam Long categoryId,

            @Parameter(description = "Specifications as JSON string", example = "{\"color\":\"blue\",\"size\":\"L\"}")
            @RequestParam(required = false) String specification,

            @Parameter(description = "Upload one or more images")
            @RequestParam(required = false) MultipartFile[] images) {

        log.info("Creating product: {}", name);

        try {
            ProductRequest request = new ProductRequest();
            request.setName(name);
            request.setCategoryId(categoryId);
            request.setImages(images);

            /** SPECIFICATION PARSING **/
            if (specification != null && !specification.isBlank()) {
                ObjectMapper mapper = new ObjectMapper();
                TypeReference<Map<String, String>> typeRef = new TypeReference<>() {
                };
                request.setSpecification(mapper.readValue(specification, typeRef));
            }

            ProductResponse response = productService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest()
                    .body(null);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(null);

        } catch (Exception e) {
            log.error("Error while creating product", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Update an existing product",
            description = "Updates product details along with optional new images and specifications."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Product updated successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),

            @ApiResponse(responseCode = "400",
                    description = "Invalid request or malformed specifications JSON",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),

            @ApiResponse(responseCode = "404",
                    description = "Product not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),

            @ApiResponse(responseCode = "500",
                    description = "Unexpected internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam Long categoryId,
            @RequestParam(required = false) String specification,
            @RequestParam(required = false) MultipartFile[] images) {

        log.info("Updating product with id: {}", id);

        try {
            ProductRequest request = new ProductRequest();
            request.setName(name);
            request.setCategoryId(categoryId);
            request.setImages(images);

            if (specification != null && !specification.isBlank()) {
                ObjectMapper mapper = new ObjectMapper();
                request.setSpecification(mapper.readValue(specification, new TypeReference<>() {
                }));
            }

            return ResponseEntity.ok(productService.update(id, request));

        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest()
                    .body(null);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(null);

        } catch (Exception e) {
            log.error("Error while updating product", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product retrieved",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(productService.get(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    @GetMapping
    @Operation(summary = "Get all products")
    public ResponseEntity<List<ProductResponse>> getAll() {
        return ResponseEntity.ok(productService.getAll());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product deleted"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            productService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    @GetMapping("/filter")
    @Operation(summary = "Filter products", description = "Filter by name, categoryId, or categoryName")
    public ResponseEntity<List<ProductResponse>> filter(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String categoryName) {

        return ResponseEntity.ok(productService.filter(name, categoryId, categoryName));
    }
}
