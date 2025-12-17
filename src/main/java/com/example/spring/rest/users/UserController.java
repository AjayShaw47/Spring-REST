package com.example.spring.rest.users;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse>  getAllUsers(){
        return userService.getAllUsers(); // 200
        // need to implement filtering, sorting, pagination
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id){
        UserResponse user = userService.getUser(id);
        return ResponseEntity.ok(user); // 200
        //        return ResponseEntity.status(HttpStatus.OK).body(user);
        //        return new ResponseEntity<>(user, HttpStatus.OK);

    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(Principal principal) {
        System.out.println("Current user: " + principal.getName());
        // Find the user details based on the principal name (username/email from token)
        UserResponse user = userService.getUser(principal.getName());
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserResponse> registerUser (@Valid @RequestBody RegisterUserRequest request){

        UserResponse savedUser =userService.registerUser(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.id()) // Replaces {id} with the actual user.getId() value
                .toUri();

        return ResponseEntity.created(location).body(savedUser); // 201 Created with Location header
        //        return ResponseEntity.status(HttpStatus.CREATED).body(user);
        //        return new ResponseEntity<>(user, HttpStatus.CREATED);

        // send jwt token after user registers?
        // var jwtToken = jwtService.generateToken(user);
        // var refreshToken = jwtService.generateRefreshToken(user);
        // put this inside auth controller inside auth package

    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request){
        /*
        User user = userService.getUser(id);
        if(user == null){
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
        userMapper.update(request,user);
        userService.registerUser(user);  // JPA first checks if a record with that ID exists in the database: If yes → UPDATE,If no → INSERT
        */
        UserResponse user = userService.patchUser(id, request);
        return ResponseEntity.ok(user); // 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
/*
    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(@PathVariable Long id,@Valid @RequestBody ChangePasswordRequest changePasswordRequest){
        User user = userService.getUser(id);
        if(user == null){
            return ResponseEntity.notFound().build();
        }

        if(!changePasswordRequest.getOldPassword().equals(user.getPassword())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        user.setPassword(changePasswordRequest.getNewPassword());
        userService.registerUser(user);

        return ResponseEntity.noContent().build();
    }
*/

}

