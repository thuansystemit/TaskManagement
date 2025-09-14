package com.darkness.userService.service;

import com.darkness.userService.domain.RefreshToken;
import com.darkness.userService.domain.User;
import com.darkness.userService.repository.RefreshTokenRepository;
import com.darkness.userService.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
/**
 * @author darkness
 **/
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshTokenServiceImpl(final RefreshTokenRepository refreshTokenRepository,
                   final UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken save(String token, User user) {
        Optional<RefreshToken> existing = refreshTokenRepository.findByUser(user);

        RefreshToken refreshToken = existing.orElse(new RefreshToken());
        refreshToken.setUser(user);
        refreshToken.setToken(token);
        refreshToken.setRevoked(false);
        refreshToken.setExpiryDate(
                Instant.now().plus(30, ChronoUnit.MINUTES));
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expired");
        }
        return token;
    }

    @Override
    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    public void revokeToken(RefreshToken token) {
        token.setRevoked(true);
        refreshTokenRepository.save(token);
    }

    // Delete all refresh tokens for user (logout everywhere)
    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    @Override
    public RefreshToken save(String token, String email) {
        User user = userRepository.findByEmail(email).get();
        return save(token, user);
    }
}
