package com.example.spring.rest.users;

import com.example.spring.rest.common.DuplicateResourceException;
import com.example.spring.rest.common.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public List<UserSummary> getAllUsers(){
        logger.debug("Fetching all users");
        return userRepository.findAllBy();
    }

    public UserResponse getUser(Long id){
        logger.debug("Fetching user by id: {}", id);
        User user = userRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("User not found with id: " + id));
        return userMapper.toDto(user);

    }

    public UserResponse getUser(String email){
        logger.debug("Fetching user by email: {}", email);
        User user = userRepository.findByEmail(email).orElseThrow(()->
                new ResourceNotFoundException("User not found with email: " + email));
        return userMapper.toDto(user);

    }

    public UserResponse registerUser(RegisterUserRequest request){
        logger.debug("Registering new user with email: {}", request.email());
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException(
                    "User with email " + request.email() + " already exists"
            );
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        User savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }

    public UserResponse patchUser(Long id, UpdateUserRequest request) {
        logger.debug("Updating user with id: {}", id);
        User user = userRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("User not found with id: " + id));

        // Update only non-null fields
        if (request.name() != null) {
            user.setName(request.name());
        }
        if (request.email() != null) {
            if (userRepository.existsByEmail(request.email())) {
                throw new DuplicateResourceException(
                        "User with email " + request.email() + " already exists"
                );
            }
            user.setEmail(request.email());
        }

        user.setUpdatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);

    }

    public void deleteUser(Long id) {
        logger.debug("Deleting user with id: {}", id);
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
    }


        /*
    Internally, deleteById() is implemented by Hibernate like this:
    Load the entity from the database → select ... from users where id=?
    Then perform the actual delete operation → delete from users where id=?
    So this is Hibernate loading the entity into the persistence context before deletion.

    Hibernate follows a managed entity lifecycle:Before deleting, it must load the entity to ensure:
        1. It exists.
        2. All cascading relationships (e.g., posts, roles) are handled properly.
        3. Any @PreRemove hooks are triggered.

    That’s why you do not need to check existsById(id), Hibernate does a SELECT by ID before DELETE.
     */

}