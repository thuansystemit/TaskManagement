package com.darkness.mailService.repository;

import com.darkness.mailService.domain.MessageAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * @author darkness
 **/
@Repository
public interface MessageAttachmentRepository extends JpaRepository<MessageAttachment, Long> {
}
