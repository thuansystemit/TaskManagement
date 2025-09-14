package com.darkness.mailService.repository;

import com.darkness.mailService.domain.MessageRecipient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * @author darkness
 **/
@Repository
public interface MessageRecipientRepository extends JpaRepository<MessageRecipient, Long> {
    List<MessageRecipient> findByUserId(Long userId);
}
