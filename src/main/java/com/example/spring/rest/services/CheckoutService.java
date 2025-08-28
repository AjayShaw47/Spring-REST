package com.example.spring.rest.services;

import com.example.spring.rest.dtos.CheckoutRequest;
import com.example.spring.rest.dtos.CheckoutResponse;
import com.example.spring.rest.dtos.ErrorDTO;
import com.example.spring.rest.entities.Cart;
import com.example.spring.rest.entities.Order;
import com.example.spring.rest.exceptions.CartEmptyException;
import com.example.spring.rest.exceptions.CartNotFoundException;
import com.example.spring.rest.repositories.CartRepository;
import com.example.spring.rest.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CheckoutService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartService cartService;

    public CheckoutResponse checkout(CheckoutRequest request){
        Cart cart = cartRepository.findById(request.getCartId()).orElse(null);
        if(cart == null)
            throw new CartNotFoundException();
        if(cart.isEmpty())
            throw new CartEmptyException();

        Order order = Order.fromCart(cart,authService.getCurrentUser());

        orderRepository.save(order);
        cartService.clearCart(cart.getId());

        return new CheckoutResponse(order.getId());
    }
}
