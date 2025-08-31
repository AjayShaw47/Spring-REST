package com.example.spring.rest.orders;

import com.example.spring.rest.payments.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderDTO {
    private Long id;
    private PaymentStatus status;
    private LocalDateTime createdAt;
    private List<OrderItemDTO> items;
    private BigDecimal totalPrice;
}
