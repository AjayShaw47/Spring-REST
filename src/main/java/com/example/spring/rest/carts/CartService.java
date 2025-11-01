package com.example.spring.rest.carts;

import com.example.spring.rest.products.Product;
import com.example.spring.rest.products.ProductNotFoundException;
import com.example.spring.rest.products.ProductRepository;
import com.example.spring.rest.users.User;
import com.example.spring.rest.users.UserRepository;
import com.example.spring.rest.users.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;

    public CartDTO createCart(Long userId){

        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found"));
        Cart cart = new Cart();
        cart.setUser(user);
        Cart savedCart =  cartRepository.save(cart); // directly saves the owning side
       return cartMapper.toDto(savedCart);
   }
   /* Note we are not doing this because User already exists in DB
    user.getCarts().add(cart);
    userRepository.save(user);
    */

    public CartItemDTO addItemToCart(UUID cartId, Long productId, Integer quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(CartNotFoundException::new);  // new CartNotFoundException()
        
        Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);

        var cartItem = cart.addItem(product,quantity);

        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    public CartDTO getCart(UUID cartId){
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if(cart == null)
            return null;
        return cartMapper.toDto(cart);
    }

    public CartItemDTO updateCart(UUID cartId, Long productId,UpdateCart request ) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(CartNotFoundException::new);

        CartItem cartItem =  cart.getItem(productId);
        if(cartItem == null)
            throw new ProductNotFoundException();
        if(request.getQuantity() != null)
            cartItem.setQuantity(request.getQuantity());
        if(request.getDeliveryOptionId() != null)
            cartItem.setDeliveryOptionId(request.getDeliveryOptionId());
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


    public PaymentSummary getPaymentSummary(UUID cartId) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(CartNotFoundException::new);

        int totalItems = cart.getItems().size();
        BigDecimal productCost =cart.getTotalPrice();
        int shippingCost = cart.getItems().stream().map(item-> {
                    if (item.getDeliveryOptionId() == 1)
                        return 0;
                    else if (item.getDeliveryOptionId() == 2)
                        return 4;
                    else
                        return 9;
                }
        ).reduce(0,Integer::sum);
        BigDecimal totalCostBeforeTax=BigDecimal.valueOf(shippingCost).add(productCost);
        BigDecimal tax =BigDecimal.valueOf(0.1).multiply(totalCostBeforeTax);
        BigDecimal totalCost= totalCostBeforeTax.add(tax);

        return new PaymentSummary(
                totalItems,
                productCost,
                shippingCost,
                totalCostBeforeTax,
                tax,
                totalCost
        );
    }

    public void clearAllCarts() {
        cartRepository.deleteAll();
    }
}
