package com.example.spring.rest.carts;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CartDTO {
    private UUID id;
    private List<CartItemDTO> items = new ArrayList<>();
    private BigDecimal totalPrice = BigDecimal.ZERO;

}
