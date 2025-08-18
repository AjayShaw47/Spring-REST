package com.example.spring.rest.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CartItemDTO {
    private CartProductDTO product;
    private Integer quantity;
    private BigDecimal totalPrice;
}
