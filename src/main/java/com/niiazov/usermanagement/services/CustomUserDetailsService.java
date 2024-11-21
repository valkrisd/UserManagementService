package com.niiazov.usermanagement.services;

import com.niiazov.usermanagement.entities.User;
import com.niiazov.usermanagement.repositories.UserRepository;
import com.niiazov.usermanagement.security.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Attempting to load user by username: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("User with username '{}' not found", username);
                    return new UsernameNotFoundException("User with username " + username + " not found");
                });

        log.info("User '{}' successfully loaded", username);
        return new CustomUserDetails(user);
    }
}

