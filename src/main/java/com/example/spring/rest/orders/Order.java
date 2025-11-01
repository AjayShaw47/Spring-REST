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
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private User customer;

    @Enumerated(EnumType.STRING)
    @Column(insertable = false)
    private OrderStatus status;

    @Column(insertable = false, updatable = false)
    private LocalDateTime createdAt;

    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "order",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<OrderItem> items = new ArrayList<>();


}
