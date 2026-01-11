package com.example.spring.rest.orders;

import com.example.spring.rest.carts.CartDTO;
import com.example.spring.rest.carts.CartService;
import com.example.spring.rest.common.ErrorDTO;
import com.example.spring.rest.users.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@AuthenticationPrincipal UserDetails userDetails){
        User user = (User)userDetails;
        OrderDTO orderDTO = orderService.createOrder(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderDTO);
    }

    @GetMapping
    public List<UserOrderResponse> getUserOrders(@AuthenticationPrincipal UserDetails userDetails){
        User user = (User)userDetails;
        return orderService.getUserOrders(user);
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

//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<ErrorDTO> handleAccessDenied(Exception ex){
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDTO(ex.getMessage()));
//    }
}
