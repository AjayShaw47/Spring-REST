package com.example.spring.rest.repositories;

import com.example.spring.rest.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart,UUID> {
}
