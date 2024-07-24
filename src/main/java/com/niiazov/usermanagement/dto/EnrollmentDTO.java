package com.niiazov.usermanagement.dto;

import com.niiazov.usermanagement.enums.EnrollmentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EnrollmentDTO {
    @NotNull(message = "User ID cannot be null")
    private Integer userId;

    @Size(max = 20, message = "Status cannot be longer than 20 characters")
    private EnrollmentStatus status;

    private LocalDate enrollmentDate;

    private Boolean completed;

    private CourseDTO courseDTO;
}

