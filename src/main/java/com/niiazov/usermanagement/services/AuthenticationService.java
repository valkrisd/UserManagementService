package com.niiazov.usermanagement.services;

import com.niiazov.usermanagement.dto.UserDTO;
import com.niiazov.usermanagement.security.CustomUserDetails;
import com.niiazov.usermanagement.security.JWTGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JWTGenerator jwtGenerator;

    public String authenticate(UserDTO userDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        return jwtGenerator.generateToken(authentication);
    }

    public Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Проверка, что аутентификация существует и principal является экземпляром CustomUserDetails
        if (authentication == null || !(authentication.getPrincipal()
                instanceof CustomUserDetails userDetails)) {
            throw new AuthenticationCredentialsNotFoundException("No authenticated user found");
        }

        // Возвращаем ID пользователя из CustomUserDetails
        return userDetails.user().getId();
    }
}
