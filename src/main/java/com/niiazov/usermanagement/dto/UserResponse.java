package com.niiazov.usermanagement.dto;

import com.niiazov.usermanagement.enums.UserStatus;
import lombok.Data;


@Data
public class UserResponse {

    private String username;
    private String email;
    private UserStatus userStatus;
}
