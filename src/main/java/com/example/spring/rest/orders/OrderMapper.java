package com.example.spring.rest.orders;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDTO toDto(Order order);
    OrderItemDTO toDto(OrderItem orderItem);
    Order toEntity(OrderDTO orderDTO);
    OrderItem toEntity(OrderItemDTO orderItemDTO);
    UserOrderResponse toUserOrderResponse(Order order);
}
