package com.example.spring.rest.dtos;

import com.example.spring.rest.entities.OrderStatus;
import com.example.spring.rest.services.OrderService;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderDTO {
    private Long id;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private List<OrderItemDTO> items;
    private BigDecimal totalPrice;
}
