package com.example.spring.rest.auth;

import com.example.spring.rest.common.ErrorDTO;
import com.example.spring.rest.users.UserMapper;
import com.example.spring.rest.users.UserResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    @Value("${jwt.refresh-expiration}")
    private int refreshExpiration;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        // 1. AuthService returns both tokens
        LoginResponse loginResponse = authService.login(request);

        // 2. Set the refresh token in a secure HttpOnly cookie
        Cookie refreshTokenCookie = new Cookie("refreshToken", loginResponse.refreshToken());
        refreshTokenCookie.setHttpOnly(true); // makes it inaccessible to JavaScript, preventing XSS attacks.
        refreshTokenCookie.setSecure(false); // only be sent across https connection Should be true in production
        refreshTokenCookie.setPath("/api/auth"); // Restricts the cookie so it’s only sent with requests to /api/auth/....
        refreshTokenCookie.setMaxAge(refreshExpiration);
        response.addCookie(refreshTokenCookie);

        // 3. Return the access token in the response body
        return ResponseEntity.ok(new AuthResponse(loginResponse.accessToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@CookieValue(value = "refreshToken") String refreshToken) {
        String newAccessToken = authService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(new AuthResponse(newAccessToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        // Clear the refresh token cookie
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Should be true in production
        cookie.setPath("/api/auth");
        cookie.setMaxAge(0); // Set max age to 0 to delete the cookie
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me() {
        var user = authService.getCurrentUser();
        return ResponseEntity.ok(user);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDTO> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        ErrorDTO apiError = new ErrorDTO(
                401,
                "UNAUTHORIZED",
                "Invalid email or password",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }
}