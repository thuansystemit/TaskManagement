package com.darkness.userService.repository;

import com.darkness.userService.domain.RefreshToken;
import com.darkness.userService.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * @author darkness
 **/
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByToken(String token);
    void deleteByUser(User user);
    Optional<RefreshToken> findByUser(User user);
}
