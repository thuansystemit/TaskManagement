package com.darkness.userService.service;

public interface TokenBlacklistService {
    void blacklistToken(String token);
    boolean isBlacklisted(String token);
}
