package com.niiazov.usermanagement.controllers;

import com.niiazov.usermanagement.dto.ProfileDTO;
import com.niiazov.usermanagement.mappers.ProfileMapper;
import com.niiazov.usermanagement.models.Profile;
import com.niiazov.usermanagement.services.ProfilesService;
import com.niiazov.usermanagement.util.ErrorsUtil;
import com.niiazov.usermanagement.util.ResourceNotUpdatedException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}/profiles")

public class ProfilesController {
    private final ProfilesService profilesService;
    private final ProfileMapper profileMapper;

    @Autowired
    public ProfilesController(ProfilesService profilesService, ProfileMapper profileMapper) {
        this.profilesService = profilesService;
        this.profileMapper = profileMapper;
    }

    // Возвращает информацию о профиле пользователя (пол, дата рождения, адрес и другие контактные данные)
    @GetMapping
    public ResponseEntity<ProfileDTO> getProfile(@PathVariable Long userId) {

        Profile profile = profilesService.findProfile(userId);
        return ResponseEntity.ok(profileMapper.profileToProfileDTO(profile));
    }

    // Обновляет информацию о профиле пользователя на основе его идентификатора.
    @PutMapping
    public ResponseEntity<HttpStatus> updateProfile(@PathVariable Long userId, @RequestBody @Valid ProfileDTO profileDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = ErrorsUtil.getErrorMessage(bindingResult);
            throw new ResourceNotUpdatedException(errorMsg);
        }

        profilesService.updateProfile(userId, profileDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
