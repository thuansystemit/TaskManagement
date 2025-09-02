package com.darkness.userService.controller;

import com.darkness.commons.security.utils.PasswordUtils;
import com.darkness.commons.jwttoken.JwtUtil;
import com.darkness.userService.domain.RefreshToken;
import com.darkness.userService.domain.User;
import com.darkness.userService.dto.TokenRefreshRequestDto;
import com.darkness.userService.exception.UserNotFoundException;
import com.darkness.userService.service.RefreshTokenService;
import com.darkness.userService.service.TokenBlacklistService;
import com.darkness.userService.service.TokenBlacklistServiceImpl;
import com.darkness.userService.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController extends UserExceptionController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordUtils passwordUtils;
    private final RefreshTokenService refreshTokenService;
    private final TokenBlacklistService tokenBlacklistService;
    public AuthController(final UserService userService,
                          final JwtUtil jwtUtil,
                          final PasswordUtils passwordUtils,
                          final RefreshTokenService refreshTokenService,
                          final TokenBlacklistService tokenBlacklistService) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordUtils = passwordUtils;
        this.refreshTokenService = refreshTokenService;
        this.tokenBlacklistService = tokenBlacklistService;
    }
    @PostMapping("login")
    public ResponseEntity<Map<String, String>> login(
            @RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordUtils.matches(password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String accessToken = jwtUtil.generateToken(user.getEmail(), user.getUserRole().name());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail(), user.getUserRole().name());
        refreshTokenService.save(refreshToken, user);
        Map<String, String> authenResponse = new HashMap<>();
        authenResponse.put("accessToken", accessToken);
        authenResponse.put("refreshToken", refreshToken);
        return ResponseEntity.ok(authenResponse);
    }

    @PostMapping("refresh")
    public ResponseEntity<Map<String, String>> refresh(
            @RequestBody TokenRefreshRequestDto request) {
        return refreshTokenService.findByToken(request.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String newAccessToken = jwtUtil.generateToken(
                            user.getEmail(), user.getUserRole().name());
                    String newRefreshToken = jwtUtil.generateRefreshToken(
                            user.getName(), user.getUserRole().name());
                    refreshTokenService.save(newRefreshToken, user);
                    Map<String, String> authenResponse = new HashMap<>();
                    authenResponse.put("accessToken", newAccessToken);
                    authenResponse.put("refreshToken", newRefreshToken);
                    return ResponseEntity.ok(authenResponse);
                })
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
    }

    @PostMapping("logout")
    public ResponseEntity<Void> logout(@RequestBody TokenRefreshRequestDto request) {
        refreshTokenService.findByToken(request.getRefreshToken())
                .ifPresent(refreshTokenService::revokeToken);
        tokenBlacklistService.blacklistToken(request.getAccessToken());
        return ResponseEntity.ok().build();
    }
}
