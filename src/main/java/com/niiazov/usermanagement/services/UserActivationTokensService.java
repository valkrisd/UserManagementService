package com.niiazov.usermanagement.services;

import com.niiazov.usermanagement.dto.UserActivationTokenDTO;
import com.niiazov.usermanagement.enums.UserStatus;
import com.niiazov.usermanagement.exceptions.ActivationException;
import com.niiazov.usermanagement.exceptions.ResourceNotFoundException;
import com.niiazov.usermanagement.models.User;
import com.niiazov.usermanagement.models.UserActivationToken;
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
    private final UserActivationTokenRepository userActivationTokensRepository;
    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.from}")
    private String EMAIL_FROM;

    @Transactional
    public void generateUserActivationToken(Integer userId) {

        User userToUpdate = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));

        log.info("Попытка генерации токена активации для пользователя с ID: {}", userId);
        UserActivationToken activationToken = createActivationToken(userToUpdate);
        log.info("Токен активации для пользователя с ID: {} успешно создан", userId);

        userToUpdate.getActivationTokens().add(activationToken);
        userActivationTokensRepository.save(activationToken);

        log.info("Попытка отправки письма активации для пользователя с ID: {}", userId);
        sendActivationEmail(userToUpdate, activationToken.getToken());
        log.info("Письмо активации для пользователя с ID: {} успешно отправлено", userId);

    }

    private UserActivationToken createActivationToken(User user) {
        UserActivationToken activationToken = new UserActivationToken();
        String token = UUID.randomUUID().toString();
        activationToken.setToken(token);

        int TOKEN_VALIDITY_IN_MINUTES = 60;
        activationToken.setExpirationTime(LocalDateTime.now().plusMinutes(TOKEN_VALIDITY_IN_MINUTES));
        activationToken.setUser(user);

        return activationToken;
    }

    private void sendActivationEmail(User user, String token) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(EMAIL_FROM);
        message.setTo(user.getEmail());
        String EMAIL_SUBJECT = "Активация аккаунта";
        message.setSubject(EMAIL_SUBJECT);
        message.setText("Пожалуйста, активируйте ваш аккаунт, используя этот токен: " + token);

        javaMailSender.send(message);
    }

    @Transactional
    public void activateUser(UserActivationTokenDTO token) {
        if (token.getToken() == null) {
            throw new ActivationException("Токен не может быть пустым");
        }

        Optional<UserActivationToken> activationTokenOpt =
                userActivationTokensRepository.findByToken(token.getToken());

        if (activationTokenOpt.isEmpty() ||
                activationTokenOpt.get().getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new ActivationException("Недействительный токен активации");
        }

        UserActivationToken activationToken = activationTokenOpt.get();
        User user = userRepository.findById(activationToken.getUser().getId())
                .orElseThrow(() -> new ActivationException("Пользователь не найден"));

        user.setUserStatus(UserStatus.ACTIVE);
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
    }

}
