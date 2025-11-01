package com.example.spring.rest.orders;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CreateOrderRequest {
    private Long customerId;
    private UUID cartId;
    private List<OrderItemDTO> items;
    private BigDecimal totalPrice;
}
