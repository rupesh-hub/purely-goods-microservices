package com.alfarays.service;

import com.alfarays.entity.Category;
import com.alfarays.entity.Image;
import com.alfarays.entity.Product;
import com.alfarays.mapper.ProductMapper;
import com.alfarays.model.ProductRequest;
import com.alfarays.model.ProductResponse;
import com.alfarays.repository.CategoryRepository;
import com.alfarays.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service for product management with image handling
 * Handles create, update, delete, and filter operations
 * Integrates with IImageService for image file management
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageService imageService;

    @Override
    @Transactional
    public ProductResponse create(ProductRequest request) {
        log.info("Creating product with name: {}", request.getName());

        // Validate request
        validateProductRequest(request);

        // Fetch and validate category
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> {
                    log.error("Category not found with id: {}", request.getCategoryId());
                    return new IllegalArgumentException("Category not found with id: " + request.getCategoryId());
                });

        Product product = new Product();
        product.setName(request.getName());
        product.setSpecification(request.getSpecification());
        product.setCategory(category);

        if (request.getImages() != null && request.getImages().length > 0) {
            Set<Image> uploadedImages = new HashSet<>();

            for (MultipartFile file : request.getImages()) {
                try {
                    Image image = imageService.save(file);
                    image.setProduct(product);
                    uploadedImages.add(image);
                    log.debug("Uploaded image: {}", file.getOriginalFilename());
                } catch (Exception e) {
                    log.error("Failed to upload image: {}", file.getOriginalFilename(), e);
                    // Rollback uploaded images if any fail
                    uploadedImages.forEach(img -> {
                        try {
                            imageService.delete(img.getId());
                        } catch (Exception rollbackEx) {
                            log.error("Failed to rollback image: {}", img.getId(), rollbackEx);
                        }
                    });
                    throw new RuntimeException("Failed to upload images during product creation", e);
                }
            }

            product.setImages(uploadedImages);
            log.info("Associated {} images to product", uploadedImages.size());
        }

        Product saved = productRepository.save(product);
        log.info("Product created successfully with id: {}", saved.getId());
        return ProductMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {
        log.info("Updating product with id: {}", id);

        validateProductRequest(request);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product not found with id: {}", id);
                    return new IllegalArgumentException("Product not found with id: " + id);
                });

        product.setName(request.getName());
        product.setSpecification(request.getSpecification());

        // Update category
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> {
                    log.error("Category not found with id: {}", request.getCategoryId());
                    return new IllegalArgumentException("Category not found with id: " + request.getCategoryId());
                });
        product.setCategory(category);

        Set<Image> oldImages = new HashSet<>(product.getImages());

        // Remove old images and delete from filesystem
        for (Image oldImage : oldImages) {
            try {
                imageService.delete(oldImage.getId());
                log.debug("Deleted old image with id: {}", oldImage.getId());
            } catch (Exception e) {
                log.error("Failed to delete old image with id: {}", oldImage.getId(), e);
                throw new RuntimeException("Failed to delete old image during update", e);
            }
        }
        product.getImages().clear();

        // Upload and associate new images
        if (request.getImages() != null && request.getImages().length > 0) {
            Set<Image> newImages = new HashSet<>();

            for (MultipartFile file : request.getImages()) {
                try {
                    Image image = imageService.save(file);
                    image.setProduct(product);
                    newImages.add(image);
                    log.debug("Uploaded new image: {}", file.getOriginalFilename());
                } catch (Exception e) {
                    log.error("Failed to upload image: {}", file.getOriginalFilename(), e);
                    // Rollback new uploaded images on failure
                    newImages.forEach(img -> {
                        try {
                            imageService.delete(img.getId());
                        } catch (Exception rollbackEx) {
                            log.error("Failed to rollback image: {}", img.getId(), rollbackEx);
                        }
                    });
                    throw new RuntimeException("Failed to upload new images during product update", e);
                }
            }

            product.setImages(newImages);
            log.info("Associated {} new images to product", newImages.size());
        }

        Product updated = productRepository.save(product);
        log.info("Product updated successfully with id: {}", updated.getId());
        return ProductMapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse get(Long id) {
        log.info("Fetching product with id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product not found with id: {}", id);
                    return new IllegalArgumentException("Product not found with id: " + id);
                });

        return ProductMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAll() {
        log.info("Fetching all products");
        return productRepository.findAll()
                .stream()
                .map(ProductMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting product with id: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product not found with id: {}", id);
                    return new IllegalArgumentException("Product not found with id: " + id);
                });

        // Delete all associated images from filesystem
        Set<Image> imagesToDelete = new HashSet<>(product.getImages());

        for (Image image : imagesToDelete) {
            try {
                imageService.delete(image.getId());
                log.debug("Deleted image with id: {} from product deletion", image.getId());
            } catch (Exception e) {
                log.error("Failed to delete image with id: {} during product deletion", image.getId(), e);
                throw new RuntimeException("Failed to delete associated images during product deletion", e);
            }
        }

        // Delete product after all images are successfully deleted
        productRepository.deleteById(id);
        log.info("Product deleted successfully with id: {}", id);
    }

    /**
     * Filter products using JPA Specification
     * Supports filtering by name, category ID, and category name
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> filter(String name, Long categoryId, String categoryName) {
        log.info("Filtering products with name: {}, categoryId: {}, categoryName: {}", name, categoryId, categoryName);

        List<Specification<Product>> specs = new ArrayList<>();

        if (name != null && !name.isBlank()) {
            specs.add((root, query, cb) ->
                    cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%")
            );
        }

        if (categoryId != null) {
            specs.add((root, query, cb) ->
                    cb.equal(root.get("category").get("id"), categoryId)
            );
        }

        if (categoryName != null && !categoryName.isBlank()) {
            specs.add((root, query, cb) ->
                    cb.like(cb.lower(root.get("category").get("name")), "%" + categoryName.toLowerCase() + "%")
            );
        }

        Specification<Product> finalSpec = Specification.allOf(specs);

        return productRepository.findAll(finalSpec)
                .stream()
                .map(ProductMapper::toResponse)
                .collect(Collectors.toList());
    }


    private void validateProductRequest(ProductRequest request) {
        if (request == null) throw new IllegalArgumentException("Product request cannot be null");
        if (request.getName() == null || request.getName().trim().isEmpty())
            throw new IllegalArgumentException("Product name cannot be empty");
        if (request.getCategoryId() == null) throw new IllegalArgumentException("Category ID cannot be null");
    }
}
