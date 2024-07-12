package com.niiazov.usermanagement.services;

import com.niiazov.usermanagement.dto.UserDTO;
import com.niiazov.usermanagement.mappers.UserMapper;
import com.niiazov.usermanagement.models.User;
import com.niiazov.usermanagement.repositories.UserRepository;
import com.niiazov.usermanagement.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public void saveUser(UserDTO userDTO) {
        User user = userMapper.userDTOToUser(userDTO);

        userRepository.save(user);
    }

    public UserDTO findUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            return userMapper.userToUserDTO(user.get());
        } else throw new ResourceNotFoundException("User with id " + userId + " not found");
    }

    @Transactional
    public void updateUser(Long userId, UserDTO userDTO) {
        User user = userMapper.userDTOToUser(userDTO);

        User userToUpdate = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User with id " + userId + " not found"));

        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setPassword(user.getPassword());
        userToUpdate.setUpdatedAt(LocalDateTime.now());

        userRepository.save(userToUpdate);
    }

    @Transactional
    public void deleteUser(Long userId) {

        User userToDelete = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User with id " + userId + " not found"));
        userRepository.delete(userToDelete);
    }
}
