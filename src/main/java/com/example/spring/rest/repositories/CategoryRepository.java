package com.example.spring.rest.repositories;

import com.example.spring.rest.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends JpaRepository<Category,Byte> {
}
