package com.niiazov.usermanagement.services;

import com.niiazov.usermanagement.dto.RoleDTO;
import com.niiazov.usermanagement.mappers.RoleMapper;
import com.niiazov.usermanagement.models.Role;
import com.niiazov.usermanagement.models.User;
import com.niiazov.usermanagement.repositories.RoleRepository;
import com.niiazov.usermanagement.repositories.UserRepository;
import com.niiazov.usermanagement.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

@Service
@RequiredArgsConstructor
public class RolesService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final RoleMapper roleMapper;

    /**
     * Добавляет роли пользователя
     *
     * @param userId    - идентификатор пользователя
     *                    (пользователь должен существовать в репозитории)
     * @param roleDTOs  - DTO с ролями пользователя
     *                    (роли должны существовать в репозитории)
     */

    @Transactional
    public void updateUserRoles(Long userId, Set<RoleDTO> roleDTOs) {

        User userToUpdate = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));

        Set<String> roleNames = roleDTOs.stream()
                .map(RoleDTO::getName)
                .collect(Collectors.toSet());

        Map<String, Role> existingRoles = roleRepository.findByNameIn(roleNames)
                .stream().collect(Collectors.toMap(Role::getName, role -> role));

        Set<Role> rolesToUpdate = new HashSet<>();
        for (RoleDTO roleDTO : roleDTOs) {
            Role role = existingRoles.get(roleDTO.getName());
            if (role == null) {
                role = new Role();
                role.setName(roleDTO.getName());
                role.setDescription(roleDTO.getDescription());
                roleRepository.save(role);
            }
            rolesToUpdate.add(role);
        }

        userToUpdate.setRoles(rolesToUpdate);
        userToUpdate.setUpdatedAt(LocalDateTime.now());

        userRepository.save(userToUpdate);
    }

    public ResponseEntity<Set<RoleDTO>> getUserRoles(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));

        return ResponseEntity.ok(user.getRoles().stream()
                .map(roleMapper::roleToRoleDTO)
                .collect(toSet()));
    }

    @Transactional
    public void deleteUserRole(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));

        user.getRoles().removeIf(role -> role.getId().equals(roleId));
    }
}
