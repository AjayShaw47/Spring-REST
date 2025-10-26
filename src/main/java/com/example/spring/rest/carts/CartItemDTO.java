package com.example.spring.rest.carts;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CartItemDTO {
    private ProductDTO product;
    private Integer quantity;
    private Integer deliveryOptionId;
    private BigDecimal totalPrice;
}
