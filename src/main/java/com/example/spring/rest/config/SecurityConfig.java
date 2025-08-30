package com.example.spring.rest.config;

import com.example.spring.rest.entities.Role;
import com.example.spring.rest.filter.JwtAuthenticationFilter;
import com.example.spring.rest.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    } // BCryptPasswordEncoder is the implementation of PasswordEncoder is the most secure and recommended password hashing algorithm

    @Bean
    public AuthenticationProvider authenticationProvider(){
        var provider  = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();

    }
    /*
    When Spring creates the AuthenticationManager via config.getAuthenticationManager(), it:
    Scans for all AuthenticationProvider beans in your application
    Automatically registers your custom AuthenticationProvider with the AuthenticationManager
    Uses your provider for authentication requests
     */


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)  // REST do not need csrf protection
                .authorizeHttpRequests(auths -> auths
                                .requestMatchers("/carts/**").permitAll()
                                .requestMatchers("/admin/**").hasRole(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.POST,"/users").permitAll()
                                .requestMatchers(HttpMethod.POST,"/auth/login").permitAll()
                                .requestMatchers(HttpMethod.POST,"/auth/refresh").permitAll()
                                .requestMatchers(HttpMethod.POST,"/checkout/webhook").permitAll()
                                .anyRequest().authenticated()
                                  // .anyRequest().permitAll()   // mentioning only this will make the endpoints as public
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                /*
                HTTP Request with: Authorization: Bearer <jwt-token>
                             ↓
                    1. JwtAuthenticationFilter (OUR CUSTOM FILTER)
                       - Extracts Bearer token
                       - Validates JWT
                       - Sets SecurityContext with user info
                       - Calls filterChain.doFilter()
                             ↓
                    2. UsernamePasswordAuthenticationFilter
                       - Sees SecurityContext already has authentication
                       - Skips its processing
                       - Calls filterChain.doFilter()
                             ↓
                    3. Other filters...
                             ↓
                    4. Your Controller Method (with authenticated user)
                 */
                .exceptionHandling(c->
                {
                    c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
                    c.accessDeniedHandler(((request, response, accessDeniedException) ->
                            response.setStatus(HttpStatus.FORBIDDEN.value())));

                });
        return http.build();
    }
}
