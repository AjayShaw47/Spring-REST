//package com.example.spring.rest.payments;
//
//import com.example.spring.rest.carts.Cart;
//import com.example.spring.rest.orders.Order;
//import com.example.spring.rest.carts.CartEmptyException;
//import com.example.spring.rest.carts.CartNotFoundException;
//import com.example.spring.rest.carts.CartRepository;
//import com.example.spring.rest.orders.OrderRepository;
//import com.example.spring.rest.auth.AuthService;
//import com.example.spring.rest.carts.CartService;
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@AllArgsConstructor
//@Service
//public class CheckoutService {
//    private final CartRepository cartRepository;
//    private final OrderRepository orderRepository;
//    private final AuthService authService;
//    private final CartService cartService;
//    private final PaymentGateway paymentGateway;
//
//    @Transactional
//    public CheckoutResponse checkout(CheckoutRequest request) {
//        Cart cart = cartRepository.findById(request.getCartId()).orElse(null);
//        if(cart == null)
//            throw new CartNotFoundException();
//        if(cart.isEmpty())
//            throw new CartEmptyException();
//
//        Order order = Order.fromCart(cart,authService.getCurrentUser());
//
//        orderRepository.save(order);
//        try{
//            var session = paymentGateway.createCheckoutSession(order);
//            cartService.clearCart(cart.getId());
//
//            return new CheckoutResponse(order.getId(),session.getCheckoutUrl());
//        }catch (PaymentException ex){
//            orderRepository.delete(order);
//            throw ex;
//        }
//
//    }
//
//    public void handleWebhookEvent(WebhookRequest request){
//        paymentGateway
//                .parseWebhookRequest(request)
//                .ifPresent( paymentResult ->{
//                    var order = orderRepository.findById(paymentResult.getOrderId()).orElseThrow();
//                    order.setStatus(paymentResult.getPaymentStatus());
//                    orderRepository.save(order);
//                });
//    }
//
//
//}
