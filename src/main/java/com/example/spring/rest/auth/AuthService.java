package com.example.spring.rest.auth;

import com.example.spring.rest.users.User;
import com.example.spring.rest.users.UserMapper;
import com.example.spring.rest.users.UserRepository;
import com.example.spring.rest.users.UserResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        var user = (UserDetails) authentication.getPrincipal();
        var jwtToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return new LoginResponse(jwtToken, refreshToken);
    }

    public UserResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return null;
        }
        var user = (User) authentication.getPrincipal();
        return userMapper.toDto(user);
    }

    public String refreshAccessToken(String refreshToken) {
        try {
            final String userEmail = jwtService.extractUsername(refreshToken);

            if (userEmail != null) {
                var userDetails = userRepository.findByEmail(userEmail)
                        .orElseThrow(() -> new BadCredentialsException("User not found from refresh token"));

                if (jwtService.isTokenValid(refreshToken, userDetails)) {
                    return jwtService.generateAccessToken(userDetails);
                }
            }
            throw new BadCredentialsException("Invalid refresh token.");

        } catch (ExpiredJwtException e) {
            throw new BadCredentialsException("Refresh token expired. Please log in again.");
        } catch (JwtException e) {
            throw new BadCredentialsException("Invalid refresh token.");
        }
    }
}