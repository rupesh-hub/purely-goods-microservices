package com.alfarays.service;

import com.alfarays.client.ProductServiceClient;
import com.alfarays.model.OrderResponse;
import com.alfarays.model.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    public boolean updateOrderStatus(String orderReference) {
        boolean isUpdated = false;
        if (orderReference != null) {
            //TODO: perform db operation
            isUpdated = true;
        }
        return isUpdated;
    }


}
