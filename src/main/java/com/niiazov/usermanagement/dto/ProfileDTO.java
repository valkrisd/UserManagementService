package com.niiazov.usermanagement.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {

    @Size(max = 255, message = "Full name must be less than 255 characters")
    private String fullName;

    @Size(max = 10, message = "Gender must be less than 10 characters")
    private String gender;


    private LocalDate dateOfBirth;

    private String address;

    @Size(max = 20, message = "Phone number must be less than 20 characters")
    private String phoneNumber;

    @Size(max = 255, message = "Avatar URL must be less than 255 characters")
    private String avatarUrl;
}
