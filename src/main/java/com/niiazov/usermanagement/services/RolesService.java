package com.niiazov.usermanagement.services;

import com.niiazov.usermanagement.dto.RoleDTO;
import com.niiazov.usermanagement.models.Role;
import com.niiazov.usermanagement.models.User;
import com.niiazov.usermanagement.repositories.RoleRepository;
import com.niiazov.usermanagement.repositories.UserRepository;
import com.niiazov.usermanagement.util.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
public class RolesService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public RolesService(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
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
}
