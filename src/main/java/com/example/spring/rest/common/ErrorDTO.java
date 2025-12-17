package com.example.spring.rest.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public record ErrorDTO(
        boolean success,
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {
    public ErrorDTO(int status, String error, String message, String path) {
        this(false, LocalDateTime.now(), status, error, message, path);
    }
}