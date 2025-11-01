package com.example.spring.rest.orders;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderItemDTO {
    private ProductDTO product;
    private Integer quantity;
    private Integer deliveryOptionId;
    private LocalDateTime estimatedDeliveryDate;
}
