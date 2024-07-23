package com.niiazov.usermanagement.dto;

import com.niiazov.usermanagement.enums.EnrollmentStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EnrollmentDTO {
    @NotEmpty(message = "User id cannot be empty")
    private Integer userId;

    @Size(max = 20, message = "Status cannot be longer than 20 characters")
    private EnrollmentStatus status;

    private LocalDate enrollmentDate;

    private Boolean completed;
}

