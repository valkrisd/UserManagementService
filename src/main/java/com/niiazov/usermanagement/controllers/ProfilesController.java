package com.niiazov.usermanagement.controllers;

import com.niiazov.usermanagement.dto.ProfileDTO;
import com.niiazov.usermanagement.services.ProfilesService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "Profiles", description = "Operations related to user profiles") // Описание контроллера
@Validated
public class ProfilesController {

    private final ProfilesService profilesService;

    @Operation(
            summary = "Get user profile",
            description = "Fetches the profile information for the specified user ID.",
            tags = {"Profiles"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile fetched successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfileDTO.class))),
            @ApiResponse(responseCode = "404", description = "Profile not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid user ID supplied", content = @Content)
    })
    @GetMapping("/{userId}/profiles")
    public ResponseEntity<ProfileDTO> getProfile(
            @Parameter(description = "ID of the user to fetch the profile for", example = "1")
            @PathVariable Integer userId) {
        log.info("Request to get profile for user id: {}", userId);
        ProfileDTO profileDTO = profilesService.findProfileDTO(userId);
        log.debug("Profile for user id: {} successfully found", userId);

        return ResponseEntity.ok(profileDTO);
    }

    @Operation(
            summary = "Update user profile",
            description = "Updates the profile information for the specified user ID.",
            tags = {"Profiles"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PutMapping("/{userId}/profiles")
    public ResponseEntity<HttpStatus> updateProfile(
            @Parameter(description = "ID of the user to update the profile for", example = "1")
            @PathVariable Integer userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Profile details to update",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfileDTO.class)),
                    required = true
            )
            @RequestBody @Valid ProfileDTO profileDTO) {
        log.info("Request to update profile for user id: {}", userId);
        profilesService.updateProfile(userId, profileDTO);
        log.debug("Profile for user id: {} successfully updated", userId);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}

