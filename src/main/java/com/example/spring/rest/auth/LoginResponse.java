package com.example.spring.rest.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

public record LoginResponse(
    String accessToken,
    String refreshToken
) {

}