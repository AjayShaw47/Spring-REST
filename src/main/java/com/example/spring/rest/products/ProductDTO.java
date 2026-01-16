package com.example.spring.rest.products;

import java.math.BigDecimal;

public record ProductDTO(
        Long id,
        String name,
        BigDecimal price,
        Integer ratingCount,
        BigDecimal ratingStar
) {
}
