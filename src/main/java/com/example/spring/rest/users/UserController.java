package com.example.spring.rest.users;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private  final PasswordEncoder passwordEncoder;


    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        List<User> users =  userService.getAllUsers();
        List<UserDTO> userDTOs = users.stream().map(userMapper::toDto).toList();

        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id){
        User user = userService.getUser(id);
        if(user == null){
            return ResponseEntity.notFound().build();  // 204 No Content
        }
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PostMapping
    public ResponseEntity<?> registerUser (@Valid @RequestBody RegisterUserRequest request){

        if(userService.emailExists(request.getEmail())){
            return ResponseEntity.badRequest().body(
                    Map.of("email","Email is already registered")
            );
        }
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        User savedUser = userService.registerUser(user);
        UserDTO userDTO = userMapper.toDto(savedUser);

        URI location = URI.create("/users/" + savedUser.getId());
        return ResponseEntity.created(location).body(userDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request){
        User user = userService.getUser(id);
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        userMapper.update(request,user);
        userService.registerUser(user);  // JPA first checks if a record with that ID exists in the database: If yes → UPDATE,If no → INSERT

        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        User user = userService.getUser(id);
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();

    }

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


}

