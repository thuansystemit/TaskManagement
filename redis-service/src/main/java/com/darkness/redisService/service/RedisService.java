package com.darkness.redisService.service;

import java.time.Duration;
import java.util.Optional;
/**
 * @author darkness
 **/
public interface RedisService {
    void setValue(String key, String value, Duration ttl);
    Optional<String> getValue(String key);
    void deleteValue(String key);
    boolean hasKey(String key);
}
