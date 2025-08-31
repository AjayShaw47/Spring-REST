package com.example.spring.rest.products;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    @EntityGraph(attributePaths = "category")
    List<Product> findByCategoryId(Byte categoryId);
}
