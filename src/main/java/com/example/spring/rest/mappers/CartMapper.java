package com.example.spring.rest.mappers;

import com.example.spring.rest.dtos.CartDTO;
import com.example.spring.rest.dtos.AddToCartItemRequest;
import com.example.spring.rest.dtos.CartItemDTO;
import com.example.spring.rest.entities.Cart;
import com.example.spring.rest.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "totalPrice",expression = "java(cart.getTotalPrice())")
    CartDTO toDto(Cart cart);

    @Mapping(target = "totalPrice",expression = "java(cartItem.getTotalPrice())")
    CartItemDTO toDto(CartItem cartItem);
}
