package com.alfarays.client;

import com.alfarays.model.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "PRODUCT-SERVICE", fallback = ProductFallback.class)
public interface ProductServiceClient {

    @GetMapping("/api/v1.0.0/products/{id}")
    ResponseEntity<ProductResponse> get(@PathVariable Long id);

}
