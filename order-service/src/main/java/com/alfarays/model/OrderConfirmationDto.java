package com.alfarays.model;

import java.time.LocalDateTime;

public record OrderConfirmationDto(
        Long productId,
        String referenceNumber,
        String status,
        LocalDateTime orderDate
) {
}
