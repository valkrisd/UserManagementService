package com.niiazov.usermanagement.controllers;

import com.niiazov.usermanagement.dto.UserActivationTokenDTO;
import com.niiazov.usermanagement.services.UserActivationTokensService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "User Activation Tokens", description = "Operations for managing user activation tokens")
public class UserActivationTokensController {

    private final UserActivationTokensService userActivationTokensService;

    @Operation(
            summary = "Generate an activation token",
            description = "Generates an activation token for a specific user ID.",
            tags = {"User Activation Tokens"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activation token generated successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid user ID", content = @Content)
    })
    @PostMapping("/{userId}/activation-token")
    public ResponseEntity<HttpStatus> generateUserActivationToken(
            @Parameter(description = "ID of the user for whom to generate the activation token", example = "1")
            @PathVariable Integer userId) {
        log.info("Try to generate activation token for user with id: {}", userId);
        userActivationTokensService.generateUserActivationToken(userId);
        log.info("Activation token successfully generated for user with id: {}", userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Activate a user",
            description = "Activates a user account using an activation token.",
            tags = {"User Activation Tokens"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User activated successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid activation token", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PostMapping("/activate")
    public ResponseEntity<HttpStatus> activateUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Activation token details",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserActivationTokenDTO.class)),
                    required = true
            )
            @RequestBody UserActivationTokenDTO userActivationTokenDTO) {
        log.info("Try to activate user");
        userActivationTokensService.activateUser(userActivationTokenDTO);
        log.info("User successfully activated");

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

