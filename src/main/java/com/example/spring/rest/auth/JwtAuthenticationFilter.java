package com.example.spring.rest.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);  // passes the response to next filter in the chain at this point spring security will kick in and if the target end point is protected it will not be accessible
            return;
        }
        var token = authHeader.replace("Bearer ", "");
        var jwt = jwtService.parseToken(token);
        if(jwt == null || jwt.isExpired()){
            filterChain.doFilter(request,response);
            return;
        } // if the code reach after this point then that means we have a valid token and now protected resources are accessible
        var authentication = new UsernamePasswordAuthenticationToken(
                jwt.getUserId(),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + jwt.getRole()))

        );
        /*
        userId - principal
        credential - null bec the user has already been authenticated via JWT token validation
         */
        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)  // attaching some additional meta data about the request
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);  // stores information about the currently authenticated user
        filterChain.doFilter(request,response);
    }
}
