package com.example.spring.rest.carts;

import com.example.spring.rest.products.ProductNotFoundException;
import com.example.spring.rest.users.User;
import com.example.spring.rest.users.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/carts")
public class CartController {

   private final CartService cartService;

    @GetMapping("/active")
    public ResponseEntity<CartDTO> getActiveCart(@AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("Authenticated active user: " + userDetails.getUsername());
        CartDTO cart = cartService.getActiveCartForUser(userDetails.getUsername());
        return ResponseEntity.ok(cart);
    }

    @PostMapping("{cartId}/items")
    public ResponseEntity<CartItemDTO> addProductToCart(@PathVariable UUID cartId, @RequestBody AddToCartItemRequest request){

         CartItemDTO cartItemDTO = cartService.addItemToCart(cartId, request.productId(), request.quantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDTO);
    }

    @PatchMapping("/{cartId}/items/{productId}")
    public ResponseEntity<CartItemDTO> updateCart(@PathVariable UUID cartId,
                                                  @PathVariable Long productId,
                                                  @RequestBody UpdateCart request) {
        System.out.println("Updating cart: " + cartId + " for product: " + productId);
        CartItemDTO cartItemDTO = cartService.updateCart(cartId, productId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDTO);

    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?>  removeProduct(@PathVariable UUID cartId,
                              @PathVariable Long productId){
        cartService.removeItem(cartId,productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{cartId}/payment-summary")
    public ResponseEntity<PaymentSummary> getPaymentSummary(@PathVariable UUID cartId){
        PaymentSummary paymentSummary = cartService.getPaymentSummary(cartId);
        return ResponseEntity.ok(paymentSummary);
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCartNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Cart not found."));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Product not found."));
    }

}
