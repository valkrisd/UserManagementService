package com.niiazov.usermanagement.controllers;

import com.niiazov.usermanagement.dto.RoleDTO;
import com.niiazov.usermanagement.services.RolesService;
import com.niiazov.usermanagement.util.ErrorsUtil;
import com.niiazov.usermanagement.exceptions.ResourceNotUpdatedException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor

public class RolesController {
    private final RolesService rolesService;

    @PostMapping("/{userId}/roles")
    public ResponseEntity<HttpStatus> updateUserRoles(@PathVariable Long userId,
                                                      @RequestBody @Valid Set<RoleDTO> roleDTOs,
                                                      BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMsg = ErrorsUtil.getErrorMessage(bindingResult);
            throw new ResourceNotUpdatedException(errorMsg);
        }

        rolesService.updateUserRoles(userId, roleDTOs);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/{userId}/roles")
    public ResponseEntity<Set<RoleDTO>> getUserRoles(@PathVariable Long userId) {

        return rolesService.getUserRoles(userId);
    }


    @DeleteMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<HttpStatus> deleteUserRole(@PathVariable Long userId,
                                                     @PathVariable Long roleId) {

        rolesService.deleteUserRole(userId, roleId);
        return ResponseEntity.ok().build();
    }


}
