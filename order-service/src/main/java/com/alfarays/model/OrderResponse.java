package com.alfarays.model;

import com.alfarays.enums.OrderStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private ProductResponse product;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String referenceNumber;

}
