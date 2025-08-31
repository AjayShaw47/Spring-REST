package com.example.spring.rest.orders;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDTO toDto(Order order);
}
