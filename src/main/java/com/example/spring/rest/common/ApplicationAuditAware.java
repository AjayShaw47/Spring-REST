package com.example.spring.rest.common;

import com.example.spring.rest.users.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class ApplicationAuditAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof User) {
            return Optional.ofNullable(((User) principal).getEmail());
        }

        // Fallback if principal is just a string (e.g. simplified tests)
        if (principal instanceof String) {
            return Optional.of((String) principal);
        }

        return Optional.empty();
    }
}
