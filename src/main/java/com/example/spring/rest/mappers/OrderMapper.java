package com.example.spring.rest.mappers;

import com.example.spring.rest.dtos.OrderDTO;
import com.example.spring.rest.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDTO toDto(Order order);
}
