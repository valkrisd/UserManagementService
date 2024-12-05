package com.niiazov.usermanagement.controllers;

import com.niiazov.usermanagement.dto.RoleDTO;
import com.niiazov.usermanagement.services.RolesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "Roles", description = "Operations related to user roles") // Описание контроллера
@Validated
public class RolesController {

    private final RolesService rolesService;

    @Operation(
            summary = "Update user roles",
            description = "Updates the roles for the specified user ID.",
            tags = {"Roles"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roles updated successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PostMapping("/{userId}/roles")
    public ResponseEntity<HttpStatus> updateUserRoles(
            @Parameter(description = "ID of the user to update roles for", example = "1")
            @PathVariable Integer userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Set of roles to assign to the user",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RoleDTO.class)),
                    required = true
            )
            @RequestBody @Valid Set<RoleDTO> roleDTOs) {
        log.info("Request to update roles for user id: {}", userId);
        rolesService.updateUserRoles(userId, roleDTOs);
        log.debug("Roles for user id: {} successfully updated: {}", userId, roleDTOs);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Get user roles",
            description = "Retrieves the roles assigned to the specified user ID.",
            tags = {"Roles"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roles fetched successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RoleDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping("/{userId}/roles")
    public ResponseEntity<Set<RoleDTO>> getUserRoles(
            @Parameter(description = "ID of the user to retrieve roles for", example = "1")
            @PathVariable Integer userId) {
        log.info("Request to get roles for user id: {}", userId);
        Set<RoleDTO> roleDTOs = rolesService.getUserRoleDTOs(userId);
        log.debug("Roles for user id: {} successfully found", userId);
        return ResponseEntity.ok(roleDTOs);
    }

    @Operation(
            summary = "Delete a user role",
            description = "Deletes a specific role for the specified user ID.",
            tags = {"Roles"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Role or user not found", content = @Content)
    })
    @DeleteMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<HttpStatus> deleteUserRole(
            @Parameter(description = "ID of the user to delete the role for", example = "1")
            @PathVariable Integer userId,
            @Parameter(description = "ID of the role to delete", example = "101")
            @PathVariable Integer roleId) {
        log.info("Request to delete role with id: {} for user with id: {}", roleId, userId);
        rolesService.deleteUserRole(userId, roleId);
        log.debug("Role with id: {} successfully deleted for user with id: {}", roleId, userId);
        return ResponseEntity.ok().build();
    }
}
