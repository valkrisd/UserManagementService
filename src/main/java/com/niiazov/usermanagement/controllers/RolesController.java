package com.niiazov.usermanagement.controllers;

import com.niiazov.usermanagement.dto.RoleDTO;
import com.niiazov.usermanagement.exceptions.ResourceNotUpdatedException;
import com.niiazov.usermanagement.services.RolesService;
import com.niiazov.usermanagement.util.ErrorsUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class RolesController {
    private final RolesService rolesService;

    @PostMapping("/{userId}/roles")
    public ResponseEntity<HttpStatus> updateUserRoles(@PathVariable Integer userId,
                                                      @RequestBody @Valid Set<RoleDTO> roleDTOs,
                                                      BindingResult bindingResult) {
        log.info("Запрос на обновление ролей пользователя с id: {}", userId);
        if (bindingResult.hasErrors()) {
            String errorMsg = ErrorsUtil.getErrorMessage(bindingResult);
            log.warn("Ошибка валидации при обновлении ролей: {}", errorMsg);
            throw new ResourceNotUpdatedException(errorMsg);
        }

        rolesService.updateUserRoles(userId, roleDTOs);
        log.debug("Роли пользователя с id: {} успешно обновлены на: {}", userId, roleDTOs);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/roles")
    public ResponseEntity<Set<RoleDTO>> getUserRoles(@PathVariable Integer userId) {
        log.info("Запрос ролей для пользователя с id: {}", userId);
        Set<RoleDTO> roleDTOs = rolesService.getUserRoles(userId);
        log.debug("Роли для пользователя с id: {} успешно получены", userId);
        return ResponseEntity.ok(roleDTOs);
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<HttpStatus> deleteUserRole(@PathVariable Integer userId, @PathVariable Integer roleId) {
        log.info("Запрос на удаление роли с id: {} у пользователя с id: {}", roleId, userId);
        rolesService.deleteUserRole(userId, roleId);
        log.debug("Роль с id: {} удалена у пользователя с id: {}", roleId, userId);
        return ResponseEntity.ok().build();
    }
}
