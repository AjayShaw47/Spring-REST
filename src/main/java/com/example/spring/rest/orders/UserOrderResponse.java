package com.example.spring.rest.orders;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class UserOrderResponse {
    private UUID id;
    private LocalDateTime createdAt;
    private List<OrderItemDTO> items;
    private BigDecimal totalPrice;
}
