package com.example.spring.rest.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDTO {
    private  OrderProductDTO product;
    private int quantity;
    private BigDecimal totalPrice;
}
