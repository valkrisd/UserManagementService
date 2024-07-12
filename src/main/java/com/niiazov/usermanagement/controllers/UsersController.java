package com.niiazov.usermanagement.controllers;

import com.niiazov.usermanagement.dto.UserDTO;
import com.niiazov.usermanagement.exceptions.ResourceNotCreatedException;
import com.niiazov.usermanagement.exceptions.ResourceNotUpdatedException;
import com.niiazov.usermanagement.mappers.UserMapper;
import com.niiazov.usermanagement.models.User;
import com.niiazov.usermanagement.services.UsersService;
import com.niiazov.usermanagement.util.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {
    private final UsersService usersService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<HttpStatus> register(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMsg = ErrorsUtil.getErrorMessage(bindingResult);
            throw new ResourceNotCreatedException(errorMsg);
        }

        usersService.saveUser(userDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    // TODO: Implement API with Spring Security later
    @PostMapping("/authenticate")
    public String authenticate(@RequestBody @Valid UserDTO userDTO) {

        return null;
    }

    // TODO: Implement API with Spring Security later
    @GetMapping("/me")
    public String me(@RequestBody @Valid UserDTO userDTO) {

        return null;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long userId) {
        User user = usersService.findUser(userId);
        return ResponseEntity.ok(userMapper.userToUserDTO(user));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<HttpStatus> updateUser(@PathVariable Long userId,
                                                 @RequestBody @Valid UserDTO userDTO,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = ErrorsUtil.getErrorMessage(bindingResult);
            throw new ResourceNotUpdatedException(errorMsg);
        }

        usersService.updateUser(userId, userDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long userId) {
        usersService.deleteUser(userId);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}



