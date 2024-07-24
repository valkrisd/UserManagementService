package com.niiazov.usermanagement.dto;

import com.niiazov.usermanagement.enums.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {
    @Size(max = 100, message = "Username must be less than 100 characters")
    private String username;

    @NotEmpty(message = "Email cannot be empty")
    @Size(max = 255, message = "Email must be less than 255 characters")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    @Size(max = 255, message = "Password must be less than 255 characters")
    private String password;

    @Size(max = 20, message = "Status must be less than 20 characters")
    private UserStatus userStatus;
}
