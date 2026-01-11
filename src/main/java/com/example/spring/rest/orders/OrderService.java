package com.example.spring.rest.orders;

import com.example.spring.rest.auth.AuthService;
import com.example.spring.rest.carts.*;
import com.example.spring.rest.users.User;
import com.example.spring.rest.users.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
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
    private final CartRepository cartRepository;

    @Transactional
    public OrderDTO createOrder(User user) {
        Cart cart = cartRepository.findByUserAndStatus(user,"ACTIVE")
                .orElseThrow(() -> new IllegalStateException("No active cart found for user"));
        Set<CartItem> cartItems = cart.getItems();
        if(cartItems.isEmpty()){
            throw new IllegalStateException("Cannot create order from empty cart");
        }
        Order order = new Order();
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PREPARING);
        order.setCustomer(user);
        order.setTotalPrice(cart.getTotalPrice());

        List<OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProduct(cartItem.getProduct());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setDeliveryOptionId(cartItem.getDeliveryOptionId());
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .toList();

        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        logger.info("Order created with ID: {} for user: {}", savedOrder.getId(), user.getEmail());
        cartRepository.delete(cart);
        return orderMapper.toDto(savedOrder);

    }

    public List<UserOrderResponse> getUserOrders(User user) {
        List<Order> orders = orderRepository.findByCustomer(user);

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
