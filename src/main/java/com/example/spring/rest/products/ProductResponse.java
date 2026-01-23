package com.example.spring.rest.products;

import java.util.List;

public record ProductResponse(
    List<ProductDTO> products,
    int currentPage,
    long totalItems,
    int totalPages
) {
}
