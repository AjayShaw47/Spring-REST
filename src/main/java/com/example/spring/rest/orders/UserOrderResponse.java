package com.example.spring.rest.orders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record UserOrderResponse(
        UUID id,
        LocalDateTime createdAt,
        List<OrderItemDTO> items,
        BigDecimal totalPrice
) {
}
