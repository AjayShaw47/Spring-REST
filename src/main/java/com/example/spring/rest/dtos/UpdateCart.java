package com.example.spring.rest.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCart {
    @NotNull(message = "quantity must be provided")
    @Min(value = 1, message = "Quantity must be greater than zero.")
    @Max(value = 1000, message = "Quantity must be less than or equal to 100.")
    private Integer quantity;
}