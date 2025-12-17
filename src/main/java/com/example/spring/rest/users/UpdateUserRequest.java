package com.example.spring.rest.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public record UpdateUserRequest (

    @Size(min = 3, max = 30, message = "Name must be between 3 and 30 characters")
    String name,

    @Email(message = "Please provide a valid email")
    String email

    // Password update should be separate endpoint
){
}
