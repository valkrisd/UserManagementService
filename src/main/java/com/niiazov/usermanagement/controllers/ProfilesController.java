package com.niiazov.usermanagement.controllers;

import com.niiazov.usermanagement.dto.ProfileDTO;
import com.niiazov.usermanagement.mappers.ProfileMapper;
import com.niiazov.usermanagement.models.Profile;
import com.niiazov.usermanagement.services.ProfilesService;
import com.niiazov.usermanagement.util.ErrorsUtil;
import com.niiazov.usermanagement.exceptions.ResourceNotUpdatedException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")

public class ProfilesController {
    private final ProfilesService profilesService;
    private final ProfileMapper profileMapper;

    @GetMapping("/{userId}/profiles")
    public ResponseEntity<ProfileDTO> getProfile(@PathVariable Long userId) {

        Profile profile = profilesService.findProfile(userId);
        return ResponseEntity.ok(profileMapper.profileToProfileDTO(profile));
    }

    @PutMapping("/{userId}/profiles")
    public ResponseEntity<HttpStatus> updateProfile(@PathVariable Long userId,
                                                    @RequestBody @Valid ProfileDTO profileDTO,
                                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = ErrorsUtil.getErrorMessage(bindingResult);
            throw new ResourceNotUpdatedException(errorMsg);
        }

        profilesService.updateProfile(userId, profileDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
