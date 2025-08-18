package com.example.spring.rest.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AddToCartItemRequest {
    private Long product_id;
}
