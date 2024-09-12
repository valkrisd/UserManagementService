package com.niiazov.usermanagement.controllers;

import com.niiazov.usermanagement.dto.UserDTO;
import com.niiazov.usermanagement.exceptions.ResourceNotCreatedException;
import com.niiazov.usermanagement.services.UsersService;
import com.niiazov.usermanagement.util.TestEntitiesBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsersControllerTests {

    @InjectMocks
    private UsersController usersController;

    @Mock
    private UsersService usersService;

    @Mock
    private BindingResult bindingResult;

    private UserDTO userDTO;

    @BeforeEach
    public void setUp() {
        userDTO = TestEntitiesBuilder.buildUserDTO();
    }

    @AfterEach
    public void tearDown() {

        Mockito.reset(usersService);
        Mockito.reset(bindingResult);
    }

    @Test
    void register_ValidUser_ReturnsOk() {
        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<HttpStatus> response = usersController.register(userDTO, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(usersService).saveUser(userDTO);
    }

    @Test
    void register_InvalidUser_ThrowsException() {
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("user", "message", "Error message")));

        assertThrows(ResourceNotCreatedException.class, () -> usersController.register(userDTO, bindingResult));

        verify(usersService, never()).saveUser(any(UserDTO.class));
    }

    @Test
    void getUser_ReturnsUserDTO() {
        Integer userId = 1;

        when(usersService.getUserDTO(userId)).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = usersController.getUser(userId);

        assertEquals(userDTO, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(usersService).getUserDTO(userId);

    }

    @Test
    void updateUser_ReturnsOk() {
        Integer userId = 1;

        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(usersService).updateUser(userId, userDTO);

        ResponseEntity<HttpStatus> response = usersController.updateUser(userId, userDTO, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(usersService).updateUser(userId, userDTO);
    }

    @Test
    void deleteUser_ReturnsOk() {
        Integer userId = 1;

        doNothing().when(usersService).deleteUser(userId);

        ResponseEntity<HttpStatus> response = usersController.deleteUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(usersService).deleteUser(userId);
    }
}
