package com.example.spring.rest.orders;

import com.example.spring.rest.common.ErrorDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody CreateOrderRequest request){
        OrderDTO orderDTO = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderDTO);
    }

    @GetMapping
    public List<UserOrderResponse> getUserOrders(){
        return orderService.getUserOrders();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable UUID orderId){
        OrderDTO orderDTO = orderService.getOrderById(orderId);
        return ResponseEntity.ok(orderDTO);
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
