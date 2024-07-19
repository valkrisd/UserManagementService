package com.niiazov.usermanagement.controllers;

import com.niiazov.usermanagement.dto.UserActivationTokenDTO;
import com.niiazov.usermanagement.services.UserActivationTokensService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j

public class UserActivationTokensController {

    private final UserActivationTokensService userActivationTokensService;

    @PostMapping("/{userId}/activation-token")
    public ResponseEntity<HttpStatus> generateUserActivationToken(@PathVariable Long userId) {
        log.info("Попытка генерации токена активации для пользователя с ID: {}", userId);
        userActivationTokensService.generateUserActivationToken(userId);
        log.info("Токен активации успешно сгенерирован для пользователя с ID: {}", userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/activate")
    public ResponseEntity<HttpStatus> activateUser(@RequestBody UserActivationTokenDTO userActivationTokenDTO) {
        log.info("Попытка активации пользователя с токеном: {}", userActivationTokenDTO.getToken());
        userActivationTokensService.activateUser(userActivationTokenDTO);
        log.info("Пользователь успешно активирован");
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
