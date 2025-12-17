package com.example.spring.rest.products;

import com.example.spring.rest.orders.OrderItem;
import com.example.spring.rest.users.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    private BigDecimal ratingStar;

    private Integer ratingCount;

    @ElementCollection
    @CollectionTable(name = "product_tags")
    @Column(name = "tag", length = 10, nullable = false)
    private Set<String> tags = new HashSet<>();

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItems = new ArrayList<>();

    @ManyToMany(mappedBy = "products")
    private Set<User> users = new HashSet<>();


}
