package com.example.spring.rest.controllers;

import com.example.spring.rest.dtos.CheckoutRequest;
import com.example.spring.rest.dtos.CheckoutResponse;
import com.example.spring.rest.dtos.ErrorDTO;
import com.example.spring.rest.exceptions.CartEmptyException;
import com.example.spring.rest.exceptions.CartNotFoundException;
import com.example.spring.rest.mappers.ProductMapper;
import com.example.spring.rest.repositories.CartRepository;
import com.example.spring.rest.services.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckOutController {

    private final CheckoutService checkoutService;

    @PostMapping
    public CheckoutResponse createOrder(@Valid @RequestBody CheckoutRequest request){
        return checkoutService.checkout(request);
    }

    @ExceptionHandler({CartNotFoundException.class, CartEmptyException.class})
    public ResponseEntity<ErrorDTO> handleException(Exception ex){
        return ResponseEntity.badRequest().body(new ErrorDTO(ex.getMessage()));
    }

}
