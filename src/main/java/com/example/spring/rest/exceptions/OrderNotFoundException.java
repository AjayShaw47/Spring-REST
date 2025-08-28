package com.example.spring.rest.exceptions;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(){
        super("Order not found");
    }
}
