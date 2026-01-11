package com.example.spring.rest.orders;

import com.example.spring.rest.products.ProductDTO;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderItemDTO(
        ProductDTO product,
        Integer quantity,
        Integer deliveryOptionId,
        LocalDateTime estimatedDeliveryDate
) {
}
