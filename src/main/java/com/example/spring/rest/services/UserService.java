package com.example.spring.rest.services;

import com.example.spring.rest.dtos.RegisterUserRequest;
import com.example.spring.rest.entities.*;
import com.example.spring.rest.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email).orElseThrow(
                ()->new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.emptyList()
        );

    }

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


}