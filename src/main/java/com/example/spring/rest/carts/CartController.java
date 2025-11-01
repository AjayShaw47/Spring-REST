package com.example.spring.rest.carts;

import com.example.spring.rest.products.ProductNotFoundException;
import com.example.spring.rest.users.User;
import com.example.spring.rest.users.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/carts")
public class CartController {

   private final CartService cartService;
   private final UserService userService;

    @PostMapping
    public ResponseEntity<CartDTO> registerCart(@RequestBody RegisterCartRequest request){

        CartDTO cartDTO =  cartService.createCart(request.getUserId());
        URI location = URI.create("/carts/" + cartDTO.getId());
        return ResponseEntity.created(location).body(cartDTO);

    }

    @GetMapping("/{userId}/items")
    public ResponseEntity<?> getCartItemsByUserId(@PathVariable Long userId){
        User user = userService.getUser(userId);
        if(user == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","Unauthorized"));
        }
        CartDTO cartDTO = cartService.getCart(user.getCarts().get(0).getId());
        return ResponseEntity.ok(cartDTO);
    }

    @PostMapping("{cartId}/items")
    public ResponseEntity<CartItemDTO> addProductToCart(@PathVariable UUID cartId, @RequestBody AddToCartItemRequest request){

         CartItemDTO cartItemDTO = cartService.addItemToCart(cartId, request.getProductId(), request.getQuantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDTO);

    }
    @GetMapping("/{cartId}")
    public ResponseEntity<CartDTO> getCart(@PathVariable UUID cartId){
        return ResponseEntity.ok(cartService.getCart(cartId));
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<CartItemDTO> updateCart(@PathVariable UUID cartId,
                                                  @PathVariable Long productId,
                                                  @RequestBody UpdateCart request) {
        CartItemDTO cartItemDTO = cartService.updateCart(cartId, productId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDTO);

    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?>  removeProduct(@PathVariable UUID cartId,
                              @PathVariable Long productId){

        cartService.removeItem(cartId,productId);
        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<?> clearCart(@PathVariable UUID cartId){
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();

    }

   @DeleteMapping
    // write a delete mapping to clear all carts from the database
    public ResponseEntity<?> clearAllCarts(){
        cartService.clearAllCarts();
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
