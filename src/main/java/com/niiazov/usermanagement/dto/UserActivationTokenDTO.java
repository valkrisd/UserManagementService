package com.niiazov.usermanagement.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserActivationTokenDTO {

    @Size(max = 255, message = "Token must be less than 255 characters")
    private String token;
}
