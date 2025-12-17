package com.example.spring.rest.users;

import lombok.AllArgsConstructor;
import lombok.Getter;

public record UserResponse(
        Long id,
        String name,
        String email
        // Don't expose password!
) {
}

