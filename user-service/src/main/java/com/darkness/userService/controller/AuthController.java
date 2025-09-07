package com.darkness.userService.controller;

import com.darkness.commons.security.utils.PasswordUtils;
import com.darkness.commons.jwttoken.JwtUtil;
import com.darkness.userService.domain.User;
import com.darkness.userService.dto.TokenRefreshRequestDto;
import com.darkness.userService.exception.UserNotFoundException;
import com.darkness.userService.service.RefreshTokenService;
import com.darkness.userService.service.TokenBlacklistService;
import com.darkness.userService.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(AuthController.REQUEST_MAPPING)
public class AuthController extends UserExceptionController {
    private static final String REFRESH_TOKEN = "refreshToken";
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String STRICT = "Strict";
    public static final String REQUEST_MAPPING = "/api/auth";
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
            @RequestBody Map<String, String> request, HttpServletResponse response) {
        String email = request.get("email");
        String password = request.get("password");
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordUtils.matches(password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String accessToken = jwtUtil.generateToken(
                user.getEmail(), user.getUserRole().name());
        String refreshToken = jwtUtil.generateRefreshToken(
                user.getEmail(), user.getUserRole().name());
        refreshTokenService.save(refreshToken, user);
        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN, refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path(REQUEST_MAPPING)
                .maxAge(Duration.ofDays(7))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        Map<String, String> authResponse = new HashMap<>();
        authResponse.put("accessToken", accessToken);

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("refresh")
    public ResponseEntity<Map<String, String>> refresh(
            HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = Arrays.stream(Optional.ofNullable(
                request.getCookies()).orElse(new Cookie[0]))
                .filter(c -> REFRESH_TOKEN.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Missing refresh token cookie"));

        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(existingToken -> {
                    User user = existingToken.getUser();

                    String newAccessToken = jwtUtil.generateToken(
                            user.getEmail(), user.getUserRole().name());

                    String newRefreshToken = jwtUtil.generateRefreshToken(
                            user.getEmail(), user.getUserRole().name());

                    refreshTokenService.revokeToken(existingToken);
                    refreshTokenService.save(newRefreshToken, user);

                    ResponseCookie cookie = ResponseCookie.from(
                            REFRESH_TOKEN, newRefreshToken)
                            .httpOnly(true)
                            .secure(true)
                            .sameSite(STRICT)
                            .path(REQUEST_MAPPING)
                            .maxAge(Duration.ofDays(7))
                            .build();
                    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

                    Map<String, String> authResponse = new HashMap<>();
                    authResponse.put(ACCESS_TOKEN, newAccessToken);

                    return ResponseEntity.ok(authResponse);
                })
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
    }

    @PostMapping("logout")
    public ResponseEntity<Void> logout(@CookieValue(
            name = "refreshToken", required = false) String refreshToken,
                 @RequestHeader(name = "Authorization", required = false)
                 String accessTokenHeader, HttpServletResponse response) {
        if (refreshToken != null) {
            refreshTokenService.findByToken(refreshToken)
                    .ifPresent(refreshTokenService::revokeToken);
        }

        if (accessTokenHeader != null && accessTokenHeader.startsWith("Bearer ")) {
            String accessToken = accessTokenHeader.substring(7);
            tokenBlacklistService.blacklistToken(accessToken);
        }

        ResponseCookie clearCookie = ResponseCookie.from(REFRESH_TOKEN, "")
                .httpOnly(true)
                .secure(true)
                .sameSite(STRICT)
                .path(REQUEST_MAPPING + "/refresh")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, clearCookie.toString());

        return ResponseEntity.ok().build();
    }
}
