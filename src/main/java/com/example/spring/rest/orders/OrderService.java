package com.example.spring.rest.orders;

import com.example.spring.rest.auth.AuthService;
import com.example.spring.rest.carts.CartRepository;
import com.example.spring.rest.carts.CartService;
import com.example.spring.rest.users.User;
import com.example.spring.rest.users.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final OrderMapper orderMapper;
    private final UserRepository userRepository;
    private final CartService cartService;

    public OrderDTO createOrder(CreateOrderRequest request){

        User user = userRepository.findById(request.getCustomerId()).orElse(null);
        if(user == null){
            throw new UsernameNotFoundException("User not found");
        }

        request.getItems().forEach(item -> logger.info("DTO deliveryOptionId: {}", item.getDeliveryOptionId()));

        List<OrderItem> items = request.getItems().stream().map(orderMapper::toEntity).toList();

        items.forEach(item -> logger.info("Entity deliveryOptionId: {}", item.getDeliveryOptionId()));

        Order order = new Order();
        order.setCustomer(user);
        order.setTotalPrice(request.getTotalPrice());
        order.setItems(items);
        order.getItems().forEach(item-> {
            item.setOrder(order);
            if(item.getDeliveryOptionId() == 1)
                item.setEstimatedDeliveryDate(LocalDateTime.now().plusDays(1));
            else if (item.getDeliveryOptionId() == 2)
                item.setEstimatedDeliveryDate(LocalDateTime.now().plusDays(3));
            else
                item.setEstimatedDeliveryDate(LocalDateTime.now().plusDays(7));
        });
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(request.getCartId());
        return orderMapper.toDto(savedOrder);
    }

    public List<UserOrderResponse> getUserOrders() {
        List<Order> orders = orderRepository.findByCustomerIdAndStatus(31L, OrderStatus.PREPARING);

        return orders.stream()
                .map(orderMapper::toUserOrderResponse)
                .toList();
    }

    public OrderDTO getOrderById(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

//        User currentUser = authService.getCurrentUser();
//
//        if (!order.getCustomer().getId().equals(currentUser.getId())) {
//            throw new AccessDeniedException("You do not have permission to access this order.");
//        }

        return orderMapper.toDto(order);
    }
}
