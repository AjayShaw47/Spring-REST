package com.example.spring.rest.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {

    @Size(min = 2, max = 50)
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    // Password update should be separate endpoint

}
