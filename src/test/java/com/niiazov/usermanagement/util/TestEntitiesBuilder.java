package com.niiazov.usermanagement.util;

import com.niiazov.usermanagement.dto.EnrollmentDTO;
import com.niiazov.usermanagement.dto.ProfileDTO;
import com.niiazov.usermanagement.dto.RoleDTO;
import com.niiazov.usermanagement.dto.UserDTO;
import com.niiazov.usermanagement.entities.Profile;
import com.niiazov.usermanagement.entities.Role;
import com.niiazov.usermanagement.entities.User;
import com.niiazov.usermanagement.enums.EnrollmentStatus;
import com.niiazov.usermanagement.enums.UserStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class TestEntitiesBuilder {


    public static User buildUser() {

        return User.builder()
                .id(1)
                .username("john")
                .email("jY8g6@example.com")
                .password("password")
                .userStatus(UserStatus.ACTIVE)
                .roles(new HashSet<>(buildUserRoles()))
                .activationTokens(new HashSet<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static User buildUserWithEmptyRolesSet() {

        return User.builder()
                .id(1)
                .username("john")
                .email("jY8g6@example.com")
                .password("password")
                .userStatus(UserStatus.ACTIVE)
                .roles(new HashSet<>())
                .activationTokens(new HashSet<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static UserDTO buildUserDTO() {

        return UserDTO.builder()
                .username("john")
                .email("jY8g6@example.com")
                .password("password")
                .userStatus(UserStatus.ACTIVE)
                .build();
    }

    public static Profile buildProfile(User user) {

        return Profile.builder()
                .id(1)
                .fullName("John Doe")
                .gender("Male")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .phoneNumber("123-456-7890")
                .avatarUrl("https://example.com/avatar.jpg")
                .user(user)
                .build();
    }


    public static ProfileDTO buildProfileDTO() {

        return ProfileDTO.builder()
                .fullName("John Doe")
                .gender("Male")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .phoneNumber("123-456-7890")
                .avatarUrl("https://example.com/avatar.jpg")
                .build();
    }

    public static Set<Role> buildUserRoles() {

        Role role1 = Role.builder()
                .id(1)
                .name("admin")
                .description("ADMIN")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Role role2 = Role.builder()
                .id(1)
                .name("user")
                .description("USER")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return Set.of(role1, role2);
    }

    public static Set<RoleDTO> buildUserRoleDTOs() {

        RoleDTO roleDTO1 = RoleDTO.builder()
                .name("admin")
                .description("ADMIN")
                .build();

        RoleDTO roleDTO2 = RoleDTO.builder()
                .name("user")
                .description("USER")
                .build();

        return Set.of(roleDTO1, roleDTO2);

    }

    public static EnrollmentDTO buildEnrollmentDTO() {

        return EnrollmentDTO.builder()
                .userId(1)
                .status(EnrollmentStatus.ACTIVE)
                .enrollmentDate(LocalDate.now())
                .completed(false)
                .courseDTO(null)
                .build();
    }


}
