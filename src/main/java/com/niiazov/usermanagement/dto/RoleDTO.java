package com.niiazov.usermanagement.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RoleDTO {

    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    private String description;
}
