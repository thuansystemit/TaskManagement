package com.darkness.mailService.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
/**
 * @author darkness
 **/
@Entity
@Table(name = "message_attachments")
@Getter
@Setter
public class MessageAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String fileType;
    private String filePath;
    private long fileSize;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_fk")
    private Message message;
}
