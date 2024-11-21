package com.niiazov.usermanagement.controllers;

import com.niiazov.usermanagement.dto.RoleDTO;
import com.niiazov.usermanagement.services.RolesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class RolesController {
    private final RolesService rolesService;

    @PostMapping("/{userId}/roles")
    public ResponseEntity<HttpStatus> updateUserRoles(@PathVariable Integer userId,
                                                      @RequestBody @Valid Set<RoleDTO> roleDTOs) {
        log.info("Request to update roles for user id: {}", userId);
        rolesService.updateUserRoles(userId, roleDTOs);
        log.debug("Roles for user id: {} successfully updated: {}", userId, roleDTOs);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/roles")
    public ResponseEntity<Set<RoleDTO>> getUserRoles(@PathVariable Integer userId) {
        log.info("Request to get roles for user id: {}", userId);
        Set<RoleDTO> roleDTOs = rolesService.getUserRoleDTOs(userId);
        log.debug("Roles for user id: {} successfully found", userId);
        return ResponseEntity.ok(roleDTOs);
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<HttpStatus> deleteUserRole(@PathVariable Integer userId,
                                                     @PathVariable Integer roleId) {
        log.info("Request to delete role with id: {} for user with id: {}", roleId, userId);
        rolesService.deleteUserRole(userId, roleId);
        log.debug("Role with id: {} successfully deleted for user with id: {}", roleId, userId);
        return ResponseEntity.ok().build();
    }
}
