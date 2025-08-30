package com.example.spring.rest.payments;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CheckoutResponse {
    private Long orderId;
    private String checkoutUrl;
}
