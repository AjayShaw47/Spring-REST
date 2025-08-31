package com.example.spring.rest.carts;

import com.example.spring.rest.products.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "date_created", insertable = false,updatable = false)  // tells hibernate to ignore this field while generating sql statement for insert and update
    @Generated  // Hibernate knows this is a generated value because of @GeneratedValue/@Generated - After INSERT, Hibernate automatically retrieves the generated ID from the database
    private LocalDate dateCreated;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.MERGE, orphanRemoval = true)  // MERGE → Updates both the Cart and all attached CartItem objects if they are already in the database,
    private Set<CartItem> items = new HashSet<>();                                    // New children will not be auto-inserted unless you also have CascadeType.PERSIST.

    public BigDecimal getTotalPrice(){
        return items.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    public CartItem getItem(Long productId){
        return items.stream()
                .filter(items->items.getProduct().getId().equals(productId))
                .findFirst().orElse(null);
    }

    public CartItem addItem(Product product){
        CartItem cartItem =  getItem(product.getId());
        if(cartItem != null){
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        }else {
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setCart(this);
            items.add(cartItem);
        }
        return cartItem;
    }

    public void removeItem(Long productId){
        var cartItem = getItem(productId);
        if(cartItem != null){
            items.remove(cartItem);

        }
    }

    public void clear(){
        items.clear();
    }

    public boolean isEmpty(){
        return items.isEmpty();
    }


}
