package com.example.spring.rest.orders;

import com.example.spring.rest.auth.AuthService;
import com.example.spring.rest.users.User;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final OrderMapper orderMapper;

    public Long createOrder(Order order){
        Order savedOrder = orderRepository.save(order);
        return savedOrder.getId();
    }

    public List<OrderDTO> getUserOrders() {
        List<Order> orders=  orderRepository.getOrdersByCustomer(authService.getCurrentUser());
        return orders.stream().map(orderMapper::toDto).toList();
    }

    public OrderDTO getOrder(Long orderId) {
        Order order = orderRepository.getOrderWithItems(orderId).orElseThrow(OrderNotFoundException::new);

        User user = authService.getCurrentUser();
        if(!order.isPlacedBy(user)){
            throw new AccessDeniedException("You don't have access to this order");
        }

        return orderMapper.toDto(order);
    }
}
