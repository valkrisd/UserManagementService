package com.niiazov.usermanagement.services;

import com.niiazov.usermanagement.models.User;
import com.niiazov.usermanagement.repositories.UserRepository;
import com.niiazov.usermanagement.util.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UsersService {
    private final UserRepository userRepository;

    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User findUser(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        return optionalUser.orElse(null);
    }

    @Transactional
    public void updateUser(Long userId, User user) {
        Optional<User> userToUpdate = userRepository.findById(userId);
        if (userToUpdate.isEmpty()) {
            throw new UserNotFoundException("User with ID " + userId + " not found");
        }
        userToUpdate.ifPresent(updatedUser -> {
            updatedUser.setUsername(user.getUsername());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setPassword(user.getPassword());
            updatedUser.setUpdatedAt(LocalDateTime.now());

            userRepository.save(updatedUser);
        });
    }
    @Transactional
    public void deleteUser(Long userId) {
        Optional<User> userToUpdate = userRepository.findById(userId);
        if (userToUpdate.isEmpty()) {
            throw new UserNotFoundException("User with id " + userId + " not found");
        }
        userToUpdate.ifPresent(userRepository::delete);
    }
}
