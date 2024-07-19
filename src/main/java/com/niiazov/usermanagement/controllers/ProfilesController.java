package com.niiazov.usermanagement.controllers;

import com.niiazov.usermanagement.dto.ProfileDTO;
import com.niiazov.usermanagement.mappers.ProfileMapper;
import com.niiazov.usermanagement.models.Profile;
import com.niiazov.usermanagement.services.ProfilesService;
import com.niiazov.usermanagement.util.ErrorsUtil;
import com.niiazov.usermanagement.exceptions.ResourceNotUpdatedException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class ProfilesController {

    private final ProfilesService profilesService;
    private final ProfileMapper profileMapper;

    @GetMapping("/{userId}/profiles")
    public ResponseEntity<ProfileDTO> getProfile(@PathVariable Integer userId) {
        log.info("Запрос профиля для пользователя с id: {}", userId);
        Profile profile = profilesService.findProfile(userId);
        log.debug("Профиль для пользователя с id: {} успешно извлечен", userId);
        return ResponseEntity.ok(profileMapper.profileToProfileDTO(profile));
    }

    @PutMapping("/{userId}/profiles")
    public ResponseEntity<HttpStatus> updateProfile(@PathVariable Integer userId, @RequestBody @Valid ProfileDTO profileDTO, BindingResult bindingResult) {
        log.info("Запрос на обновление профиля для пользователя с id: {}", userId);
        if (bindingResult.hasErrors()) {
            String errorMsg = ErrorsUtil.getErrorMessage(bindingResult);
            log.warn("Ошибка валидации при обновлении профиля: {}", errorMsg);
            throw new ResourceNotUpdatedException(errorMsg);
        }
        profilesService.updateProfile(userId, profileDTO);
        log.debug("Профиль для пользователя с id: {} успешно обновлен", userId);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}