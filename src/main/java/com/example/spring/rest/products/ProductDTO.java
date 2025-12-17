package com.example.spring.rest.products;

import java.math.BigDecimal;

public record ProductDTO(
        Long id,
        String name,
        BigDecimal price,
        Byte categoryId,
        Integer ratingCount,
        BigDecimal ratingStar
) {
}
