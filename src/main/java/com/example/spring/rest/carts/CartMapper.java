package com.example.spring.rest.carts;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "totalPrice",expression = "java(cart.getTotalPrice())")
    CartDTO toDto(Cart cart);

    @Mapping(target = "totalPrice",expression = "java(cartItem.getTotalPrice())")
    CartItemDTO toDto(CartItem cartItem);

    Cart toEntity(CartDTO cartDTO);
}
