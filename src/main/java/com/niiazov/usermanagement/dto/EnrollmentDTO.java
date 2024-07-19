package com.niiazov.usermanagement.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
@Data
public class EnrollmentDTO {
    @NotNull
    private Integer userId;

    private LocalDate enrollmentDate;

    private Boolean completed;

}
