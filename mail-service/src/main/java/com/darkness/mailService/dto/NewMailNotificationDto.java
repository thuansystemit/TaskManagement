package com.darkness.mailService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author darkness
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewMailNotificationDto {
    private Long messageId;
    private String subject;
    private Long senderId;
}
