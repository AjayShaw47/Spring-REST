package com.example.spring.rest.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CartProductDTO {
    private Long id;
    private String name;
    private BigDecimal price;
}
