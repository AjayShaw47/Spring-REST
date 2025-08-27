package com.example.spring.rest.controllers;

import com.example.spring.rest.config.JwtConfig;
import com.example.spring.rest.dtos.JwtResponse;
import com.example.spring.rest.dtos.LoginRequest;
import com.example.spring.rest.dtos.UserDTO;
import com.example.spring.rest.mappers.UserMapper;
import com.example.spring.rest.repositories.UserRepository;
import com.example.spring.rest.services.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtConfig jwtConfig;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> logIn(@Valid @RequestBody LoginRequest request,
                                             HttpServletResponse response){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        var cookie = new Cookie("refreshToken", refreshToken.toString()) ;
        cookie.setHttpOnly(true);  // can not be accessed by js
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge (jwtConfig.getRefreshTokenExpiration());
        cookie.setSecure(true); // only be sent across https connection
        response.addCookie(cookie);

        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
    }
    // We are delegating the act of user authentication to authentication manager instead of manually doing it(check bkp)

    @PostMapping("/refresh")

    public ResponseEntity<JwtResponse> refresh(
            @CookieValue(value = "refreshToken") String refreshToken) {
        var jwt = jwtService.parseToken(refreshToken);
        if (jwt == null || jwt.isExpired()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var user = userRepository.findById(jwt.getUserId()).orElseThrow();
        var accessToken = jwtService.generateAccessToken(user);

        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> me() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();

        var user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        var userDto = userMapper.toDto(user);

        return ResponseEntity.ok(userDto);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentialException(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    // when the login end point fails due to incorrect credentials it is returning error 403(forbidden)
    // so we handle this using ExceptionHandler and return error status 401(unauthorize) instead

}


