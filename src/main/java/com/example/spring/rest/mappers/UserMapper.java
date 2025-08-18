package com.example.spring.rest.mappers;

import com.example.spring.rest.dtos.RegisterUserRequest;
import com.example.spring.rest.dtos.UpdateUserRequest;
import com.example.spring.rest.dtos.UserDTO;
import com.example.spring.rest.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDto(User user);
    User toEntity(RegisterUserRequest request);
    void update(UpdateUserRequest request, @MappingTarget User user);
}