package com.example.spring.rest.services;

import com.example.spring.rest.dtos.CartDTO;
import com.example.spring.rest.dtos.CartItemDTO;
import com.example.spring.rest.entities.Cart;
import com.example.spring.rest.entities.CartItem;
import com.example.spring.rest.entities.Product;
import com.example.spring.rest.exceptions.CartNotFoundException;
import com.example.spring.rest.exceptions.ProductNotFoundException;
import com.example.spring.rest.mappers.CartMapper;
import com.example.spring.rest.repositories.CartRepository;
import com.example.spring.rest.repositories.ProductRepository;
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

    }

    public void clearCart(UUID cartId) {
        Cart cart =  cartRepository.findById(cartId)
                .orElseThrow(CartNotFoundException::new);
        cart.clear();
        cartRepository.save(cart);

    }



}
