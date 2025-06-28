package com.api.mediaapi.config.authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationUserProvider {

    public Long getUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserPrincipal userPrincipal) {
            return userPrincipal.getUserId();
        }
        return null;
    }
}
