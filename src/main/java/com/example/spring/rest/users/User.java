package com.example.spring.rest.users;

import com.example.spring.rest.carts.Cart;
import com.example.spring.rest.products.Product;
import jakarta.persistence.*;
import lombok.*;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)  // stores this value as string in db
    private Role role;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST,CascadeType.REMOVE},orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST,CascadeType.REMOVE},orphanRemoval = true)
    private List<Cart> carts = new ArrayList<>();


    @ManyToMany
    @JoinTable(
            name = "wishlist",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products = new HashSet<>();

//    @OneToMany(mappedBy = "customer")
//    private List<OrderItem> orders = new ArrayList<>();

    /*
    The relation is a bidirectional one-to-many: one User can have many Address entries.
    In User the collection is the inverse (non-owning) side because of mappedBy = "user".
    Cascade PERSIST and REMOVE mean addresses are saved/removed with the user;
    orphanRemoval = true deletes an address if it’s removed from the addresses list.

    // Add these helpers to the User entity:
    public void addAddress(Address address) {
        addresses.add(address);
        address.setUser(this);
    }

    public void removeAddress(Address address) {
        addresses.remove(address);
        address.setUser(null);
    }

     */
}
