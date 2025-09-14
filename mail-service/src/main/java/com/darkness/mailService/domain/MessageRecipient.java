package com.darkness.mailService.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
/**
 * @author darkness
 **/
@Entity
@Table(name = "message_recipients")
@Getter
@Setter
public class MessageRecipient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    @Enumerated(EnumType.ORDINAL)
    private RecipientType type;
    @Enumerated(EnumType.ORDINAL)
    private MessageStatus status = MessageStatus.SENT;
    private LocalDateTime readAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_fk")
    private Message message;
}