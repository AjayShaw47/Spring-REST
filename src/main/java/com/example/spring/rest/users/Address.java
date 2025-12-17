package com.example.spring.rest.users;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
public class Address {
    @Column(length = 100,nullable = false)
    private String street;
    @Column(length = 50,nullable = false)
    private String city;
    @Column(length = 20,nullable = false)
    private String pinCode;
    @Column(length = 50,nullable = false)
    private String state;
    @Column(length = 50,nullable = false)
    private String country;
}
