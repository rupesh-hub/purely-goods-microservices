package com.alfarays.mapper;

import com.alfarays.entity.Category;
import com.alfarays.model.CategoryResponse;

public final class CategoryMapper {

    private CategoryMapper(){}

    public static CategoryResponse toResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        return response;
    }
}
