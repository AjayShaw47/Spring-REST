package com.example.spring.rest.services;

import com.example.spring.rest.dtos.RegisterUserRequest;
import com.example.spring.rest.entities.*;
import com.example.spring.rest.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUser(Long id){
        return userRepository.findById(id).orElse(null);

    }

    public User registerUser(User user){
        return userRepository.save(user);
    }

    public boolean emailExists(String email){
        return userRepository.existsByEmail(email);
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

/*
    UserResponse convertToUserDTO(User user){
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    User convertToUser (RegisterUserRequest userRequest){
       User user = new User();
       user.setName(userRequest.getName());
       user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());

        return user;
    }

 */



}