package com.example.spring.rest.orders;

import com.example.spring.rest.carts.Cart;
import com.example.spring.rest.payments.PaymentStatus;
import com.example.spring.rest.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @Enumerated(EnumType.STRING)  // Store as string instead of ordinal
    private PaymentStatus status;

    @Column(insertable = false, updatable = false)
    private LocalDateTime createdAt;
    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "order",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})

    private List<OrderItem> items = new ArrayList<>();

    public static Order fromCart(Cart cart, User customer){

        Order order= new Order();
        order.setCustomer(customer);
        order.setStatus(PaymentStatus.PENDING);
        order.setTotalPrice(cart.getTotalPrice());


        cart.getItems().forEach(item -> {
            var orderItem = new OrderItem(order,item.getProduct(),item.getQuantity());
            order.items.add(orderItem);
        });
        return order;
    }

    public boolean isPlacedBy(User customer){
        return this.customer.equals(customer);
    }

}
