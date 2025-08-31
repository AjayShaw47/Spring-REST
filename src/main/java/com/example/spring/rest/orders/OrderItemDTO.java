package com.example.spring.rest.orders;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDTO {
    private ProductDTO product;
    private int quantity;
    private BigDecimal totalPrice;
}
