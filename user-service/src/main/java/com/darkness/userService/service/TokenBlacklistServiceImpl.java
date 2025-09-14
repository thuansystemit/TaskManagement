package com.darkness.userService.service;

import com.darkness.redisService.service.RedisService;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * @author darkness
 **/
@Service
public class TokenBlacklistServiceImpl implements TokenBlacklistService {
    private final RedisService redisService;

    public TokenBlacklistServiceImpl(final RedisService redisService) {
        this.redisService = redisService;
    }
    @Override
    public void blacklistToken(String token) {
        redisService.setValue("blacklist:" + token, "1", Duration.ofHours(1));
    }

    @Override
    public boolean isBlacklisted(String token) {
        return redisService.hasKey("blacklist:" + token);
    }
}
