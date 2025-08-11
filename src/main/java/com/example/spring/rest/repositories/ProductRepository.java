package com.example.spring.rest.repositories;

import com.example.spring.rest.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
