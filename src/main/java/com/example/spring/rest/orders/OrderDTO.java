package com.example.spring.rest.orders;

import com.example.spring.rest.payments.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderDTO(
        UUID id,
        Long customerId,
        OrderStatus status,
        LocalDateTime createdAt,
        List<OrderItemDTO> items,
        BigDecimal totalPrice
) {
}
