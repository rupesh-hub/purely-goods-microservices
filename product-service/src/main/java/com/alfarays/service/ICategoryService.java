package com.alfarays.service;

import com.alfarays.model.CategoryRequest;
import com.alfarays.model.CategoryResponse;

import java.util.List;

public interface ICategoryService {
    CategoryResponse create(CategoryRequest request);
    CategoryResponse update(Long id, CategoryRequest request);
    CategoryResponse get(Long id);
    List<CategoryResponse> getAll();
    void delete(Long id);
}
