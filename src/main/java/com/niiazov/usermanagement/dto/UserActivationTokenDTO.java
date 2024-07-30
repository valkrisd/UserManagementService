package com.niiazov.usermanagement.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserActivationTokenDTO {

    @Size(max = 255, message = "Token must be less than 255 characters")
    private String token;
}
