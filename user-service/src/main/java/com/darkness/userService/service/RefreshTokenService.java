package com.darkness.userService.service;

import com.darkness.userService.domain.RefreshToken;
import com.darkness.userService.domain.User;

import java.util.Optional;

public interface RefreshTokenService {
    Optional<RefreshToken> findByToken(String token);
    RefreshToken save(String token, User user);
    void deleteByToken(String token);
    RefreshToken verifyExpiration(RefreshToken token);
    void revokeToken(RefreshToken token);
    void deleteByUser(User user);
    RefreshToken save(String token, String email);
}
