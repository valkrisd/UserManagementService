package com.niiazov.usermanagement.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserActivationTokenDTO {
    @Size(max = 255, message = "Token must be less than 255 characters")
    private String token;

    private LocalDateTime expirationTime;
}
