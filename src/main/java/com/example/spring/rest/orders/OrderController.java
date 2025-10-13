package com.example.spring.rest.orders;

import com.example.spring.rest.common.ErrorDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

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
