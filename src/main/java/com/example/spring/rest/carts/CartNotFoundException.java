package com.example.spring.rest.carts;

public class CartNotFoundException extends RuntimeException{
    public CartNotFoundException(){
        super("Cart not found");
    }
}
