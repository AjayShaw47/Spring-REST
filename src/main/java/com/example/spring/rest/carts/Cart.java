package com.example.spring.rest.carts;

import com.example.spring.rest.products.Product;
import com.example.spring.rest.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "RAW(16)")
    private UUID id;

    @Column(name = "status",insertable = false, length = 10, nullable = false)
    private String status;

    @Column(name = "date_created", insertable = false,updatable = false)
    private LocalDate dateCreated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @OneToMany(mappedBy = "cart",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            orphanRemoval = true)
    @OrderBy("createdAt ASC")
    private Set<CartItem> items = new LinkedHashSet<>();

    /*
  `@OrderBy("createdAt ASC")`: This annotation works at the database level. It tells Hibernate to add an ORDER BY clause to the SQL query when it fetches the cart items.
   This ensures the items are retrieved from the database already sorted by their creation time.
  `LinkedHashSet`: This works at the application level. Its job is to preserve the order of the items as they are added to it.

  If you were to remove @OrderBy, the database would return the items in an arbitrary order, and the LinkedHashSet would just preserve that incorrect, arbitrary order.
  You need both to guarantee your cart items are always sorted correctly.
  */

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

    public CartItem addItem(Product product, Integer quantity){
        CartItem cartItem =  getItem(product.getId());
        if(cartItem != null){
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        }else {
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
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
