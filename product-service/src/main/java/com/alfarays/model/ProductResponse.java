package com.alfarays.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * DTO for product response
 * Includes complete product information with images and category
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private Long id;
    private String name;
    private Map<String, String> specification;

    private CategoryResponse category;
    private List<ImageResponse> images;
}