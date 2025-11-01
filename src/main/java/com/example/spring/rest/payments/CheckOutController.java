//package com.example.spring.rest.payments;
//
//import com.example.spring.rest.common.ErrorDTO;
//import com.example.spring.rest.carts.CartEmptyException;
//import com.example.spring.rest.carts.CartNotFoundException;
//import com.example.spring.rest.orders.OrderRepository;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/checkout")
//public class CheckOutController {
//
//    private final CheckoutService checkoutService;
//    private final OrderRepository orderRepository;
//
//    @PostMapping
//    public CheckoutResponse createOrder(@Valid @RequestBody CheckoutRequest request) {
//            return checkoutService.checkout(request);
//
//    }
//
//    @PostMapping("/webhook")
//    public void handleWebhook(
//            @RequestHeader Map<String,String> headers,
//            @RequestBody String payload
//
//    ){
//        checkoutService.handleWebhookEvent(new WebhookRequest(headers,payload));
//    }
//
//    @ExceptionHandler(PaymentException.class)
//    public ResponseEntity<?> handlePaymentException(){
//        return ResponseEntity
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(new ErrorDTO("Error creating a checkout session"));
//    }
//
//    @ExceptionHandler({CartNotFoundException.class, CartEmptyException.class})
//    public ResponseEntity<ErrorDTO> handleException(Exception ex){
//        return ResponseEntity.badRequest().body(new ErrorDTO(ex.getMessage()));
//    }
//
//}
