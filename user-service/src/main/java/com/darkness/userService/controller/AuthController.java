package com.darkness.userService.controller;

import com.darkness.commons.security.utils.PasswordUtils;
import com.darkness.mvc.controller.BaseController;
import com.darkness.userService.common.JwtUtil;
import com.darkness.userService.domain.User;
import com.darkness.userService.exception.UserNotFoundException;
import com.darkness.userService.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController extends UserExceptionController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordUtils passwordUtils;
    public AuthController(final UserService userService,
                          final JwtUtil jwtUtil,
                          final PasswordUtils passwordUtils) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordUtils = passwordUtils;
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordUtils.matches(password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = jwtUtil.generateToken(user);
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }
}
