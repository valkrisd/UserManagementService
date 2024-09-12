package com.niiazov.usermanagement.dto;

import com.niiazov.usermanagement.enums.EnrollmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class EnrollmentDTO {

    @NotNull(message = "User ID cannot be null")
    private Integer userId;

    private EnrollmentStatus status;

    private LocalDate enrollmentDate;

    private Boolean completed;

    private CourseDTO courseDTO;
}

