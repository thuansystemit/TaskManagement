package com.darkness.commons.security.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
/**
 * @author darkness
 **/
@Component
public class PasswordUtils {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // strength default = 10
    }

    // Encrypt password
    public String encodePassword(String rawPassword) {
        return passwordEncoder().encode(rawPassword);
    }

    // Verify password
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder().matches(rawPassword, encodedPassword);
    }
}