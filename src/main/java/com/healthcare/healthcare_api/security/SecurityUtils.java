package com.healthcare.healthcare_api.security;

import com.healthcare.healthcare_api.security.jwt.AuthenticatedUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static AuthenticatedUser currentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof AuthenticatedUser user)) {
            throw new IllegalStateException("No authenticated user found.");
        }
        return user;
    }

    public static Long currentUserId() {
        return currentUser().getUserId();
    }

    public static String currentUsername() {
        return currentUser().getUsername();
    }
}
