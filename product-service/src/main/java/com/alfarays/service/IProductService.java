package com.alfarays.service;

import com.alfarays.model.ProductRequest;
import com.alfarays.model.ProductResponse;

import java.util.List;

public interface IProductService {
    ProductResponse create(ProductRequest request);
    ProductResponse update(Long id, ProductRequest request);
    ProductResponse get(Long id);
    List<ProductResponse> getAll();
    void delete(Long id);
    List<ProductResponse> filter(String name, Long categoryId, String categoryName);
}
