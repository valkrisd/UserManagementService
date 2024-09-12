package com.niiazov.usermanagement.services;

import com.niiazov.usermanagement.dto.RoleDTO;
import com.niiazov.usermanagement.entities.Role;
import com.niiazov.usermanagement.entities.User;
import com.niiazov.usermanagement.repositories.RoleRepository;
import com.niiazov.usermanagement.repositories.UserRepository;
import com.niiazov.usermanagement.util.TestEntitiesBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RolesServiceTests {
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UsersService userService;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RolesService rolesService;

    private User user;
    private User emptyRolesUser;
    private Set<RoleDTO> roleDTOs;


    @BeforeEach
    public void setUp() {

        roleDTOs = TestEntitiesBuilder.buildUserRoleDTOs();
        emptyRolesUser = TestEntitiesBuilder.buildUserWithEmptyRolesSet();
        user = TestEntitiesBuilder.buildUser();
    }

    @AfterEach
    public void tearDown() {

        Mockito.reset(roleRepository);
        Mockito.reset(userService);
        Mockito.reset(userRepository);
    }

    @Test
    void testIsUserRolesUpdated_whenUserHasNoRoles() {

        userRepository.save(emptyRolesUser);

        when(userService.getUser(emptyRolesUser.getId())).thenReturn(emptyRolesUser);
        when(roleRepository.findByNameIn(anySet())).thenReturn(emptyRolesUser.getRoles().stream().toList());
        when(roleRepository.save(any(Role.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userRepository.save(any(User.class))).thenReturn(emptyRolesUser);

        rolesService.updateUserRoles(emptyRolesUser.getId(), roleDTOs);

        Set<String> updatedUserRolesNames = emptyRolesUser.getRoles().stream()
                .map(Role::getName)
                .collect(toSet());

        Assertions.assertEquals(updatedUserRolesNames, roleDTOs.stream()
                .map(RoleDTO::getName).collect(toSet()));

    }

    @Test
    void testIsUserRolesUpdated() {

        userRepository.save(user);
        Set<Role> userRoles = user.getRoles();

        when(userService.getUser(user.getId())).thenReturn(user);
        when(roleRepository.findByNameIn(anySet())).thenReturn(userRoles.stream().toList());

        rolesService.updateUserRoles(user.getId(), roleDTOs);

        Set<String> updatedUserRolesNames = userRoles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        Assertions.assertEquals(updatedUserRolesNames, roleDTOs.stream()
                .map(RoleDTO::getName).collect(Collectors.toSet()));
    }

}

