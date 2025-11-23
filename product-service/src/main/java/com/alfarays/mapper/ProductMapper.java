package com.alfarays.mapper;

import com.alfarays.entity.Category;
import com.alfarays.entity.Image;
import com.alfarays.entity.Product;
import com.alfarays.model.CategoryResponse;
import com.alfarays.model.ImageResponse;
import com.alfarays.model.ProductResponse;

import java.util.stream.Collectors;

public final class ProductMapper {

    private ProductMapper() {
    }

    public static ProductResponse toResponse(Product product) {
        if (product == null) return null;

        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setSpecification(product.getSpecification());

        if (product.getCategory() != null) {
            response.setCategory(mapCategory(product.getCategory()));
        }

        if (product.getImages() != null && !product.getImages().isEmpty()) {
            response.setImages(
                    product.getImages().stream()
                            .map(ProductMapper::mapImage)
                            .collect(Collectors.toList())
            );
        }

        return response;
    }

    private static CategoryResponse mapCategory(Category category) {
        if (category == null) return null;

        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        return response;
    }

    private static ImageResponse mapImage(Image image) {
        if (image == null) return null;

        ImageResponse response = new ImageResponse();
        response.setId(image.getId());
        response.setPath(image.getUrl());
        response.setName(image.getName());
        response.setOriginalName(image.getOriginalName());
        response.setContentType(image.getContentType());
        response.setSize(image.getSize());
        return response;
    }

}
