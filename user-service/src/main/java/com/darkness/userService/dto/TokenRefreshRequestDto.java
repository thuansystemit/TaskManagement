package com.darkness.userService.dto;

import lombok.Getter;
import lombok.Setter;
/**
 * @author darkness
 **/
@Getter
@Setter
public class TokenRefreshRequestDto {
    private String refreshToken;
    private String accessToken;
}
