package com.niiazov.usermanagement.controllers;

import com.niiazov.usermanagement.services.UserActivationTokensService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor

public class UserActivationTokensController {

    private final UserActivationTokensService userActivationTokensService;

    @PostMapping("/{userId}/activation-token")
    public ResponseEntity<HttpStatus> generateUserActivationToken(@PathVariable Long userId) {

        userActivationTokensService.generateUserActivationToken(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/activate")
    public ResponseEntity<HttpStatus> activateUser() {

        userActivationTokensService.activateUser();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
