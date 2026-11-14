package com.healthcare.healthcare_auth.util;

import com.healthcare.healthcare_auth.security.AuthenticatedUser;
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
        return currentUser().getUser().getId();
    }

    public static String currentUsername() {
        return currentUser().getUsername();
    }

    public static String currentUserEmail() {
        return currentUser().getUser().getEmail();
    }
}
