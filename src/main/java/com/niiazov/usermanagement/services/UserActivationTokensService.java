package com.niiazov.usermanagement.services;

import com.niiazov.usermanagement.dto.UserActivationTokenDTO;
import com.niiazov.usermanagement.entities.User;
import com.niiazov.usermanagement.entities.UserActivationToken;
import com.niiazov.usermanagement.enums.UserStatus;
import com.niiazov.usermanagement.exceptions.ActivationException;
import com.niiazov.usermanagement.exceptions.ResourceNotFoundException;
import com.niiazov.usermanagement.repositories.UserActivationTokenRepository;
import com.niiazov.usermanagement.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserActivationTokensService {
    private static final String EMAIL_SUBJECT = "Активация аккаунта";
    private static final String EMAIL_TEXT = "Ваш код активации: ";
    private static final int TOKEN_VALIDITY_IN_MINUTES = 60;
    private final UserActivationTokenRepository userActivationTokensRepository;
    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.from}")
    private String EMAIL_FROM;

    @Transactional
    public void generateUserActivationToken(Integer userId) {

        User userToUpdate = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));

        if (userToUpdate.getUserStatus().equals(UserStatus.ACTIVE)) {
            throw new ActivationException("User with id " + userId + " is already activated");
        }

        log.info("Try to create activation token for user with id: {}", userId);
        UserActivationToken activationToken = createActivationToken(userToUpdate);
        log.info("Activation token successfully created for user with id: {}", userId);

        userToUpdate.getActivationTokens().add(activationToken);
        userActivationTokensRepository.save(activationToken);

        log.info("Try to send activation email for user with id: {}", userId);
        sendActivationEmail(userToUpdate, activationToken.getToken());
        log.info("Activation email successfully sent for user with id: {}", userId);

    }

    private UserActivationToken createActivationToken(User user) {
        UserActivationToken activationToken = new UserActivationToken();
        String token = UUID.randomUUID().toString();
        activationToken.setToken(token);

        activationToken.setExpirationTime(LocalDateTime.now().plusMinutes(TOKEN_VALIDITY_IN_MINUTES));
        activationToken.setUser(user);

        return activationToken;
    }

    private void sendActivationEmail(User user, String token) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(EMAIL_FROM);
        message.setTo(user.getEmail());
        message.setSubject(EMAIL_SUBJECT);
        message.setText(EMAIL_TEXT + token);

        javaMailSender.send(message);
    }

    @Transactional
    public void activateUser(UserActivationTokenDTO token) {

        if (token.getToken().isEmpty()) {
            throw new ActivationException("Token is empty");
        }

        Optional<UserActivationToken> activationTokenOpt =
                userActivationTokensRepository.findByToken(token.getToken());

        if (activationTokenOpt.isEmpty() ||
                activationTokenOpt.get().getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new ActivationException("Token is invalid");
        }

        UserActivationToken activationToken = activationTokenOpt.get();
        User user = userRepository.findById(activationToken.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + activationToken.getUser().getId() + " not found"));

        user.setUserStatus(UserStatus.ACTIVE);
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
    }

}
