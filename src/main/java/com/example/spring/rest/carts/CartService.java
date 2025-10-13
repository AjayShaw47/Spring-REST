package com.example.spring.rest.carts;

import com.example.spring.rest.products.Product;
import com.example.spring.rest.products.ProductNotFoundException;
import com.example.spring.rest.products.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    public CartDTO createCart(){
        Cart cart = new Cart();
        Cart savedCart =  cartRepository.save(cart);
       return cartMapper.toDto(savedCart);
   }

    public CartItemDTO addItemToCart(UUID cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(CartNotFoundException::new);  // new CartNotFoundException()
        
        Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);

        var cartItem = cart.addItem(product);

        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    public CartDTO getCart(UUID cartId){
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if(cart == null)
            return null;
        return cartMapper.toDto(cart);
    }

    public CartItemDTO updateCart(UUID cartId, Long productId,Integer quantity ) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(CartNotFoundException::new);

        CartItem cartItem =  cart.getItem(productId);
        if(cartItem == null)
            throw new ProductNotFoundException();
        cartItem.setQuantity(quantity);
        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    public void removeItem(UUID cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(CartNotFoundException::new);

        cart.removeItem(productId);
        cartRepository.save(cart); // Save the changes to persist the removal
    }

    public void clearCart(UUID cartId) {
        Cart cart =  cartRepository.findById(cartId)
                .orElseThrow(CartNotFoundException::new);
        cart.clear();
        cartRepository.save(cart);

    }



}
