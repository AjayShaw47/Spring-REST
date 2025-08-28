package com.example.spring.rest.controllers;

import com.example.spring.rest.dtos.ErrorDTO;
import com.example.spring.rest.dtos.OrderDTO;
import com.example.spring.rest.exceptions.OrderNotFoundException;
import com.example.spring.rest.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.security.AccessControlException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    @GetMapping
    public List<OrderDTO> getOrders(){
        return orderService.getUserOrders();
    }

    @GetMapping("/orderId")
    public OrderDTO getOrder(@PathVariable Long orderId){
        return orderService.getOrder(orderId);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Void> handleOrderNotFound(){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDTO> handleAccessDenied(Exception ex){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDTO(ex.getMessage()));
    }
}
