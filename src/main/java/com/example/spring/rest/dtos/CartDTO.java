package com.example.spring.rest.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
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
