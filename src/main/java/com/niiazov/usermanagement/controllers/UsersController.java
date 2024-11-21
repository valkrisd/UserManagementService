package com.niiazov.usermanagement.controllers;

import com.niiazov.usermanagement.dto.AuthResponseDTO;
import com.niiazov.usermanagement.dto.UserDTO;
import com.niiazov.usermanagement.services.AuthenticationService;
import com.niiazov.usermanagement.services.UsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UsersController {
    private final UsersService usersService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<HttpStatus> register(@RequestBody @Valid UserDTO userDTO) {

        log.info("Registration user: {}", userDTO.getUsername());
        usersService.saveUser(userDTO);
        log.info("User successfully registered: {}", userDTO.getUsername());

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> authenticate(@RequestBody @Valid UserDTO userDTO) {

        log.info("Authentication user: {}", userDTO.getUsername());
        String token = authenticationService.authenticate(userDTO);
        log.info("User successfully authenticated: {}", userDTO.getUsername());

        return ResponseEntity.ok(new AuthResponseDTO(token));
    }

    @GetMapping("/me")
    public UserDTO me() {

        log.info("Getting me..");
        return usersService.getUserDTO(authenticationService.getCurrentUserId());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Integer userId) {

        log.info("Searching user with id: {}", userId);
        UserDTO userDTO = usersService.getUserDTO(userId);
        log.info("User with id: {} successfully found", userId);

        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<HttpStatus> updateUser(@PathVariable Integer userId,
                                                 @RequestBody @Valid UserDTO userDTO) {
        log.info("Updating user with id: {}", userId);
        usersService.updateUser(userId, userDTO);
        log.info("User with id: {} successfully updated", userId);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Integer userId) {
        log.info("Trying to delete user with id: {}", userId);
        usersService.deleteUser(userId);
        log.info("User with id: {} successfully deleted", userId);

        return ResponseEntity.ok(HttpStatus.OK);
    }
}



