package com.example.spring.rest.carts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class PaymentSummary {
    private int totalItems;
    private BigDecimal productCost;
    private int shippingCost;
    private BigDecimal totalCostBeforeTax;
    private BigDecimal tax;
    private BigDecimal totalCost;
}
