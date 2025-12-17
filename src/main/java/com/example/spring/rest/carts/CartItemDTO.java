package com.example.spring.rest.carts;

import com.example.spring.rest.products.ProductDTO;
import java.math.BigDecimal;

public record CartItemDTO(
        ProductDTO product,
        Integer quantity,
        Integer deliveryOptionId,
        BigDecimal totalPrice
) {
}
