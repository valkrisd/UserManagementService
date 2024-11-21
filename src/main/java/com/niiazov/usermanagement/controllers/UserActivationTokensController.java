package com.niiazov.usermanagement.controllers;

import com.niiazov.usermanagement.dto.UserActivationTokenDTO;
import com.niiazov.usermanagement.services.UserActivationTokensService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserActivationTokensController {

    private final UserActivationTokensService userActivationTokensService;

    @PostMapping("/{userId}/activation-token")
    public ResponseEntity<HttpStatus> generateUserActivationToken(@PathVariable Integer userId) {
        log.info("Try to generate activation token for user with id: {}", userId);
        userActivationTokensService.generateUserActivationToken(userId);
        log.info("Activation token successfully generated for user with id: {}", userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/activate")
    public ResponseEntity<HttpStatus> activateUser(@RequestBody UserActivationTokenDTO userActivationTokenDTO) {
        log.info("Try to activate user");
        userActivationTokensService.activateUser(userActivationTokenDTO);
        log.info("User successfully activated");

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
