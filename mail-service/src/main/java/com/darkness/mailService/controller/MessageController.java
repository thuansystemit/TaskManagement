package com.darkness.mailService.controller;

import com.darkness.mailService.domain.Message;
import com.darkness.mailService.domain.MessageAttachment;
import com.darkness.mailService.domain.MessageRecipient;
import com.darkness.mailService.security.CurrentUserUtil;
import com.darkness.mailService.service.FileStorageService;
import com.darkness.mailService.service.MessageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
/**
 * @author darkness
 **/
@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;
    private final FileStorageService fileStorageService;

    public MessageController(final MessageService messageService,
                             final FileStorageService fileStorageService) {
        this.messageService = messageService;
        this.fileStorageService = fileStorageService;
    }
    @PostMapping(value = "/send", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Message> sendMessage(
            @RequestParam List<Long> to,
            @RequestParam(required = false) List<Long> cc,
            @RequestParam(required = false) List<Long> bcc,
            @RequestParam String subject,
            @RequestParam String body,
            @RequestParam(required = false) List<MultipartFile> attachments
    ) throws IOException {
        Long senderId = CurrentUserUtil.getCurrentUserId();
        return ResponseEntity.ok(messageService.sendMessage(senderId, to, cc, bcc, subject, body, attachments));
    }

    @GetMapping("/inbox")
    public ResponseEntity<List<MessageRecipient>> getInbox() {
        Long userId = CurrentUserUtil.getCurrentUserId();
        return ResponseEntity.ok(messageService.getInbox(userId));
    }

    @PostMapping("/{recipientId}/read")
    public ResponseEntity<MessageRecipient> markAsRead(@PathVariable Long recipientId) {
        Long userId = CurrentUserUtil.getCurrentUserId();
        return ResponseEntity.ok(messageService.markAsRead(recipientId, userId));
    }

    @GetMapping("/attachments/{id}")
    public ResponseEntity<Resource> downloadAttachment(@PathVariable Long id) throws MalformedURLException {
        MessageAttachment attachment = messageService.getAttachment(id);
        Resource file = fileStorageService.loadFile(attachment.getFilePath());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(attachment.getFileType()))
                .body(file);
    }
}
