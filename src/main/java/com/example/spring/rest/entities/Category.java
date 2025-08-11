package com.example.spring.rest.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String  name;

    @OneToMany(mappedBy = "category",cascade = {CascadeType.PERSIST})
    @Builder.Default
    private List<Product> products = new ArrayList<>();

}
