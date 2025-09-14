package com.darkness.userService.service;
/**
 * @author darkness
 **/
public interface TokenBlacklistService {
    void blacklistToken(String token);
    boolean isBlacklisted(String token);
}
