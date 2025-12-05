package com.alfarays.client;

import com.alfarays.model.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductFallback implements ProductServiceClient {

    @Override
    public ResponseEntity<ProductResponse> get(Long id) {
       throw new RuntimeException("Product fetch error!");
    }

}
