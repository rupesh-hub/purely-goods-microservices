package com.alfarays.resource;

import com.alfarays.client.ProductServiceClient;
import com.alfarays.model.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderResource {

    private final ProductServiceClient productService;

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductResponse> productById(@PathVariable("productId") Long productId) {
        return productService.get(productId);
    }


}
