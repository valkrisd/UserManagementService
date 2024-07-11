package com.niiazov.usermanagement.controllers;

import com.niiazov.usermanagement.dto.RoleDTO;
import com.niiazov.usermanagement.services.RolesService;
import com.niiazov.usermanagement.util.ErrorsUtil;
import com.niiazov.usermanagement.util.ResourceNotUpdatedException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/users/{userId}/roles")
public class RolesController {
    private final RolesService rolesService;
    @Autowired
    public RolesController(RolesService rolesService) {
        this.rolesService = rolesService;
    }

    // Назначает новую роль или обновляет существующие роли для указанного пользователя.
    @PostMapping
    public ResponseEntity<HttpStatus> updateUserRoles(@PathVariable Long userId, @RequestBody @Valid Set<RoleDTO> roleDTOs, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMsg = ErrorsUtil.getErrorMessage(bindingResult);
            throw new ResourceNotUpdatedException(errorMsg);
        }

        rolesService.updateUserRoles(userId, roleDTOs);
        return ResponseEntity.ok().build();

    }

    // Возвращает список всех ролей, назначенных указанному пользователю
    @GetMapping
    public ResponseEntity<Set<RoleDTO>> getUserRoles(@PathVariable Long userId) {

        return rolesService.getUserRoles(userId);
    }

    // Удаляет указанную роль у пользователя
    @DeleteMapping("/{roleId}")
    public ResponseEntity<HttpStatus> deleteUserRole(@PathVariable Long userId, @PathVariable Long roleId) {

        rolesService.deleteUserRole(userId, roleId);
        return ResponseEntity.ok().build();
    }


}
