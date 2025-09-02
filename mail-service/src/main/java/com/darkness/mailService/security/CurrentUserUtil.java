package com.darkness.mailService.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentUserUtil {

    /**
     * Returns the ID of the currently authenticated user.
     * Assumes that the Authentication principal stores the user ID as a String.
     */
    public static Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw new RuntimeException("User not authenticated");
        }

        // If you are using JWT, the principal can be the user ID
        // or a UserDetails object from which you can get the ID
        return Long.parseLong(auth.getName());
    }
}
