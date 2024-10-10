package com.niiazov.usermanagement.controllers;

import com.niiazov.usermanagement.dto.UserDTO;
import com.niiazov.usermanagement.services.UsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {
    private final UsersService usersService;

    // TODO: Add password encryption later
    @PostMapping("/register")
    public ResponseEntity<HttpStatus> register(@RequestBody @Valid UserDTO userDTO) {

        log.info("Registration user: {}", userDTO);
        usersService.saveUser(userDTO);
        log.info("User successfully registered: {}", userDTO);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    // TODO: Implement API with Spring Security later
    @PostMapping("/authenticate")
    public String authenticate(@RequestBody @Valid UserDTO userDTO) {

        log.info("Authentication user: {}", userDTO);
        return "Test response";
    }

    // TODO: Implement API with Spring Security later
    @GetMapping("/me")
    public String me(@RequestBody @Valid UserDTO userDTO) {

        log.info("Get me: {}", userDTO);
        return "Test response";
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



