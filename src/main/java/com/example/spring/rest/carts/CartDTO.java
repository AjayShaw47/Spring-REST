package com.example.spring.rest.carts;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CartDTO(
        UUID id,
        List<CartItemDTO> items,
        BigDecimal totalPrice
) {
}
