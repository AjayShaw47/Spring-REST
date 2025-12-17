package com.example.spring.rest.carts;

import com.example.spring.rest.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart,UUID> {
    Optional<Cart> findByUserAndStatus(User user, String status);
}
