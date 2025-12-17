package com.example.spring.rest.carts;

import lombok.Getter;
import lombok.Setter;

public record AddToCartItemRequest( Long productId,Integer quantity) {
}
