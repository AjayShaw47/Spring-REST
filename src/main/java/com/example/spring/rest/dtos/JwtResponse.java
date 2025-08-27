package com.example.spring.rest.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class JwtResponse {
    private String token;
}

