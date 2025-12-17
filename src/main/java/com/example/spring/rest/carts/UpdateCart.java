package com.example.spring.rest.carts;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public record UpdateCart( Integer quantity,Integer deliveryOptionId) {

}