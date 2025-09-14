package com.darkness.mailService.service;

import com.darkness.mailService.domain.Message;
import com.darkness.mailService.domain.MessageAttachment;
import com.darkness.mailService.domain.MessageRecipient;
import com.darkness.mailService.domain.MessageStatus;
import com.darkness.mailService.domain.RecipientType;
import com.darkness.mailService.dto.NewMailNotificationDto;
import com.darkness.mailService.repository.MessageAttachmentRepository;
import com.darkness.mailService.repository.MessageRecipientRepository;
import com.darkness.mailService.repository.MessageRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
/**
 * @author darkness
 **/
@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageRecipientRepository recipientRepository;
    private final MessageAttachmentRepository attachmentRepository;
    private final FileStorageService fileStorageService;
    private final SimpMessagingTemplate messagingTemplate;

    public MessageService(final MessageRepository messageRepository,
                          final MessageRecipientRepository recipientRepository,
                          final MessageAttachmentRepository attachmentRepository,
                          final FileStorageService fileStorageService,
                          final SimpMessagingTemplate messagingTemplate) {
        this.messageRepository = messageRepository;
        this.recipientRepository = recipientRepository;
        this.attachmentRepository = attachmentRepository;
        this.fileStorageService = fileStorageService;
        this.messagingTemplate = messagingTemplate;
    }
    public Message sendMessage(Long senderId,
                               List<Long> toRecipients,
                               List<Long> ccRecipients,
                               List<Long> bccRecipients,
                               String subject,
                               String body,
                               List<MultipartFile> files) throws IOException {

        Message message = new Message();
        message.setSenderId(senderId);
        message.setSubject(subject);
        message.setBody(body);

        // Add recipients
        toRecipients.forEach(userId -> message.addRecipient(new MessageRecipient() {{
            setUserId(userId); setType(RecipientType.TO);
        }}));
        ccRecipients.forEach(userId -> message.addRecipient(new MessageRecipient() {{
            setUserId(userId); setType(RecipientType.CC);
        }}));
        bccRecipients.forEach(userId -> message.addRecipient(new MessageRecipient() {{
            setUserId(userId); setType(RecipientType.BCC);
        }}));

        // Save attachments
        if (files != null) {
            for (MultipartFile file : files) {
                String path = fileStorageService.saveFile(file);
                message.addAttachment(new MessageAttachment() {{
                    setFileName(file.getOriginalFilename());
                    setFileType(file.getContentType());
                    setFileSize(file.getSize());
                    setFilePath(path);
                }});
            }
        }

        Message saved = messageRepository.save(message);

        // Notify recipients via WebSocket
        saved.getRecipients().forEach(r -> {
            messagingTemplate.convertAndSend("/topic/messages/" + r.getUserId(),
                    new NewMailNotificationDto(saved.getPk(), saved.getSubject(), saved.getSenderId()));
        });

        return saved;
    }

    public List<MessageRecipient> getInbox(Long userId) {
        return recipientRepository.findByUserId(userId);
    }

    public MessageRecipient markAsRead(Long recipientId, Long userId) {
        MessageRecipient recipient = recipientRepository.findById(recipientId)
                .orElseThrow(() -> new RuntimeException("Recipient not found"));
        if (!recipient.getUserId().equals(userId))
            throw new RuntimeException("Cannot read someone else's message");

        recipient.setStatus(MessageStatus.READ);
        recipient.setReadAt(LocalDateTime.now());
        return recipientRepository.save(recipient);
    }
    public MessageAttachment getAttachment(Long attachmentId) {
        return attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new RuntimeException("Attachment not found"));
    }
}
