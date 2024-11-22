package com.niiazov.usermanagement.services;

import com.niiazov.usermanagement.dto.UserDTO;
import com.niiazov.usermanagement.entities.User;
import com.niiazov.usermanagement.exceptions.ResourceNotFoundException;
import com.niiazov.usermanagement.mappers.UserMapper;
import com.niiazov.usermanagement.repositories.RoleRepository;
import com.niiazov.usermanagement.repositories.UserRepository;
import com.niiazov.usermanagement.util.TestEntitiesBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsersServiceTests {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UsersService usersService;

    private User user;
    private UserDTO userDTO;


    @BeforeEach
    public void setUp() {

        user = TestEntitiesBuilder.buildUser();
        userDTO = TestEntitiesBuilder.buildUserDTO();
    }

    @AfterEach
    public void tearDown() {

        Mockito.reset(userRepository);
        Mockito.reset(userMapper);
    }

    @Test
    public void testSaveUser() {

        when(userMapper.userDTOToUser(userDTO)).thenReturn(user);
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(user.getPassword())).thenReturn(user.getPassword());
        when(roleRepository.findByName(Mockito.any(String.class))).thenReturn(user.getRoles().stream().findFirst());

        usersService.saveUser(userDTO);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        Assertions.assertEquals(user.getUsername(), capturedUser.getUsername());
        Assertions.assertEquals(user.getEmail(), capturedUser.getEmail());

    }

    @Test
    public void testGetUserDTO() {

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userMapper.userToUserDTO(user)).thenReturn(userDTO);

        UserDTO foundUserDTO = usersService.getUserDTO(user.getId());

        Assertions.assertEquals(userDTO, foundUserDTO);

    }

    @Test
    public void testGetUserDTO_WhenUserDoesNotExist() {

        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, ()
                -> usersService.getUserDTO(user.getId()));
    }

    @Test
    public void testGetUser() {

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User foundUser = usersService.getUser(user.getId());

        Assertions.assertEquals(user, foundUser);

    }

    @Test
    public void testGetUser_WhenUserDoesNotExist() {

        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, ()
                -> usersService.getUser(user.getId()));

    }

    @Test
    public void testUserIsUpdated() {

        when(userMapper.userDTOToUser(userDTO)).thenReturn(user);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        usersService.updateUser(user.getId(), userDTO);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        Assertions.assertEquals(user.getId(), capturedUser.getId());
    }

    @Test
    public void testUserIsDeleted() {

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        usersService.deleteUser(user.getId());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).delete(captor.capture());

        Assertions.assertEquals(user.getId(), captor.getValue().getId(), "Original user ID should match the deleted user ID");

    }

}
