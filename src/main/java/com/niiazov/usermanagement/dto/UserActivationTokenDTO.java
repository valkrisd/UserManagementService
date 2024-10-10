package com.niiazov.usermanagement.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserActivationTokenDTO {

    @Size(max = 255, message = "Token must be less than 255 characters")
    private String token;
}
