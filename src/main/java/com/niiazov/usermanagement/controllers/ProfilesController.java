package com.niiazov.usermanagement.controllers;

import com.niiazov.usermanagement.dto.ProfileDTO;
import com.niiazov.usermanagement.services.ProfilesService;
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
public class ProfilesController {

    private final ProfilesService profilesService;

    @GetMapping("/{userId}/profiles")
    public ResponseEntity<ProfileDTO> getProfile(@PathVariable Integer userId) {
        log.info("Request to get profile for user id: {}", userId);
        ProfileDTO profileDTO = profilesService.findProfileDTO(userId);
        log.debug("Profile for user id: {} successfully found", userId);

        return ResponseEntity.ok(profileDTO);
    }

    @PutMapping("/{userId}/profiles")
    public ResponseEntity<HttpStatus> updateProfile(@PathVariable Integer userId,
                                                    @RequestBody @Valid ProfileDTO profileDTO) {
        log.info("Request to update profile for user id: {}", userId);
        profilesService.updateProfile(userId, profileDTO);
        log.debug("Profile for user id: {} successfully updated", userId);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}