package com.darkness.commons.dto.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
/**
 * @author darkness
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long pk;
    private String userId;
    @NotEmpty(message = "Email must not be empty")
    private String email;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String userRole;
    @NotEmpty(message = "Password must not empty")
    private String password;
    @NotNull(message = "Identification is required")
    @Valid
    private IdentificationDto identificationDto;
}
