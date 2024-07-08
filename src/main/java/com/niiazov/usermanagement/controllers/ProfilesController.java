package com.niiazov.usermanagement.controllers;

import com.niiazov.usermanagement.dto.ProfileDTO;
import com.niiazov.usermanagement.mappers.ProfileMapper;
import com.niiazov.usermanagement.models.Profile;
import com.niiazov.usermanagement.models.User;
import com.niiazov.usermanagement.services.ProfilesService;
import com.niiazov.usermanagement.services.UsersService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}/profiles")
@AllArgsConstructor
public class ProfilesController {
    private final ProfilesService profilesService;
    private final ProfileMapper profileMapper;

    @GetMapping
    public ResponseEntity<ProfileDTO> getProfile(@PathVariable Long userId) {
        Profile profile = profilesService.findProfile(userId);

        if (profile != null) return ResponseEntity.ok(profileMapper.profileToProfileDTO(profile));
        else return ResponseEntity.notFound().build();
    }
    @PutMapping
    public ResponseEntity<HttpStatus> updateProfile(@PathVariable Long userId, @RequestBody @Valid ProfileDTO profileDTO, BindingResult bindingResult) {
        Profile profile = profileMapper.profileDTOToProfile(profileDTO);

        if (bindingResult.hasErrors()) return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);

        profilesService.updateProfile(userId, profile);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
