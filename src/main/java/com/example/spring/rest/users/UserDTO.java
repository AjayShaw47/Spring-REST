package com.example.spring.rest.users;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserDTO {
    private Long id;
    private String name;
    private String email;
}
