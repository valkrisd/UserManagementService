package com.niiazov.usermanagement.services;

import com.niiazov.usermanagement.dto.UserDTO;
import com.niiazov.usermanagement.dto.UserResponse;
import com.niiazov.usermanagement.entities.Role;
import com.niiazov.usermanagement.entities.User;
import com.niiazov.usermanagement.enums.UserStatus;
import com.niiazov.usermanagement.exceptions.ResourceNotFoundException;
import com.niiazov.usermanagement.mappers.UserMapper;
import com.niiazov.usermanagement.repositories.RoleRepository;
import com.niiazov.usermanagement.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Transactional
    public void saveUser(UserDTO userDTO) {

        User user = userMapper.userDTOToUser(userDTO);

        user.setUserStatus(UserStatus.PENDING);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Получаем роль "USER" из базы данных
        Role defaultRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new ResourceNotFoundException("Default role USER not found"));

        // Связываем пользователя с ролью
        user.setRoles(Set.of(defaultRole));

        userRepository.save(user);
    }

    public UserDTO getUserDTO(Integer userId) {

        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            return userMapper.userToUserDTO(user.get());
        } else throw new ResourceNotFoundException("User with id " + userId + " not found");
    }

    public User getUser(Integer userId) {

        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            return user.get();
        } else throw new ResourceNotFoundException("User with id " + userId + " not found");
    }

    public UserResponse getUserResponse(Integer userId) {

        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            return userMapper.userToUserResponse(user.get());
        } else throw new ResourceNotFoundException("User with id " + userId + " not found");
    }

    @Transactional
    public void updateUser(Integer userId, UserDTO userDTO) {

        User user = userMapper.userDTOToUser(userDTO);

        User userToUpdate = getUser(userId);

        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setPassword(user.getPassword());

        userRepository.save(userToUpdate);
    }

    @Transactional
    public void deleteUser(Integer userId) {

        User userToDelete = getUser(userId);

        userRepository.delete(userToDelete);
    }

}
