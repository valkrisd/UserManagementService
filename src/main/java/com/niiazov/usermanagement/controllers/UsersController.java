package com.niiazov.usermanagement.controllers;

import com.niiazov.usermanagement.dto.AuthResponseDTO;
import com.niiazov.usermanagement.dto.UserDTO;
import com.niiazov.usermanagement.dto.UserResponse;
import com.niiazov.usermanagement.services.AuthenticationService;
import com.niiazov.usermanagement.services.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Users", description = "Operations for managing users")
public class UsersController {

    private final UsersService usersService;
    private final AuthenticationService authenticationService;

    @Operation(
            summary = "Register a new user",
            description = "Register a new user with the provided credentials.",
            tags = {"Users"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully registered", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid user data", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<HttpStatus> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User data for registration",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class)),
                    required = true
            )
            @RequestBody @Valid UserDTO userDTO) {

        log.info("Registration user: {}", userDTO.getUsername());
        usersService.saveUser(userDTO);
        log.info("User successfully registered: {}", userDTO.getUsername());

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(
            summary = "Authenticate a user",
            description = "Authenticate a user with username and password, and return a JWT token.",
            tags = {"Users"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Invalid username or password", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> authenticate(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User credentials for authentication",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class)),
                    required = true
            )
            @RequestBody @Valid UserDTO userDTO) {

        log.info("Authentication user: {}", userDTO.getUsername());
        String token = authenticationService.authenticate(userDTO);
        log.info("User successfully authenticated: {}", userDTO.getUsername());

        return ResponseEntity.ok(new AuthResponseDTO(token));
    }

    @Operation(
            summary = "Get current user details",
            description = "Retrieve the details of the currently authenticated user.",
            tags = {"Users"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User details retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping("/me")
    public ResponseEntity<UserResponse> me() {
        log.info("Getting me..");
        UserResponse userResponse = usersService.getUserResponse(authenticationService.getCurrentUserId());
        log.info("Me successfully found");

        return ResponseEntity.ok(userResponse);
    }

    @Operation(
            summary = "Get user by ID",
            description = "Retrieve the details of a specific user by their ID.",
            tags = {"Users"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User details retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(
            @Parameter(description = "ID of the user to retrieve", example = "1")
            @PathVariable Integer userId) {

        log.info("Searching user with id: {}", userId);
        UserResponse userResponse = usersService.getUserResponse(userId);
        log.info("User with id: {} successfully found", userId);

        return ResponseEntity.ok(userResponse);
    }

    @Operation(
            summary = "Update user details",
            description = "Update the details of a specific user by their ID.",
            tags = {"Users"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully updated", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid user data", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PutMapping("/{userId}")
    public ResponseEntity<HttpStatus> updateUser(
            @Parameter(description = "ID of the user to update", example = "1")
            @PathVariable Integer userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated user data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class)),
                    required = true
            )
            @RequestBody @Valid UserDTO userDTO) {

        log.info("Updating user with id: {}", userId);
        usersService.updateUser(userId, userDTO);
        log.info("User with id: {} successfully updated", userId);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(
            summary = "Delete user by ID",
            description = "Delete a specific user by their ID.",
            tags = {"Users"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<HttpStatus> deleteUser(
            @Parameter(description = "ID of the user to delete", example = "1")
            @PathVariable Integer userId) {

        log.info("Trying to delete user with id: {}", userId);
        usersService.deleteUser(userId);
        log.info("User with id: {} successfully deleted", userId);

        return ResponseEntity.ok(HttpStatus.OK);
    }
}




