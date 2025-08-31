package com.example.spring.rest.carts;

import com.example.spring.rest.products.ProductNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {

   private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartDTO> registerCart(){
        CartDTO cartDTO =  cartService.createCart();
        URI location = URI.create("/carts/" + cartDTO.getId());
        return ResponseEntity.created(location).body(cartDTO);

    }

    @PostMapping("{cartId}/items")
    public ResponseEntity<CartItemDTO> addProductToCart(@PathVariable UUID cartId, @RequestBody AddToCartItemRequest request){

         CartItemDTO cartItemDTO = cartService.addItemToCart(cartId, request.getProduct_id());
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
        CartItemDTO cartItemDTO = cartService.updateCart(cartId, productId, request.getQuantity());
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

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCartNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Cart not found."));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Product not found."));
    }


}
