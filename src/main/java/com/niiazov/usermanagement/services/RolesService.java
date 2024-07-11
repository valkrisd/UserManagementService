package com.niiazov.usermanagement.services;

import com.niiazov.usermanagement.dto.RoleDTO;
import com.niiazov.usermanagement.mappers.RoleMapper;
import com.niiazov.usermanagement.models.Role;
import com.niiazov.usermanagement.models.User;
import com.niiazov.usermanagement.repositories.RoleRepository;
import com.niiazov.usermanagement.repositories.UserRepository;
import com.niiazov.usermanagement.util.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
public class RolesService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final RoleMapper roleMapper;

    @Autowired
    public RolesService(RoleRepository roleRepository, UserRepository userRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.roleMapper = roleMapper;
    }

    @Transactional
    public void updateUserRoles(Long userId, Set<RoleDTO> roleDTOs) {

        User userToUpdate = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));

        Set<Role> rolesToUpdate = roleDTOs.stream()
                .map(dto -> roleRepository.findByName(dto.getName()).orElseGet(() -> {

                    Role newRole = new Role();
                    newRole.setName(dto.getName());
                    newRole.setDescription(dto.getDescription());
                    return roleRepository.save(newRole);
                }))
                .collect(toSet());

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
