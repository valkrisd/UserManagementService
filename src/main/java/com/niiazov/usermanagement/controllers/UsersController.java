package com.niiazov.usermanagement.controllers;

import com.niiazov.usermanagement.dto.UserDTO;
import com.niiazov.usermanagement.mappers.UserMapper;
import com.niiazov.usermanagement.models.User;
import com.niiazov.usermanagement.services.UsersService;
import com.niiazov.usermanagement.util.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {
    private final UsersService usersService;
    private final UserMapper userMapper;

    @Autowired
    public UsersController(UsersService usersService, UserMapper userMapper, UserMapper userMapper1) {
        this.usersService = usersService;
        this.userMapper = userMapper1;
    }

    // Создает новую учетную запись пользователя на основе предоставленных данных (имя, электронная почта, пароль и другие).
    @PostMapping("/register")
    public ResponseEntity<HttpStatus> register(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMsg = ErrorsUtil.getErrorMessage(bindingResult);
            throw new ResourceNotCreatedException(errorMsg);
        }

        usersService.saveUser(userDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    // TODO: Implement API with Spring Security later
    // Проверяет учетные данные пользователя (электронная почта и пароль) и возвращает токен доступа (JWT) для аутентификации в других сервисах
    @PostMapping("/authenticate")
    public String authenticate(@RequestBody @Valid UserDTO userDTO) {

        return null;
    }

    // TODO: Implement API with Spring Security later
    // Возвращает информацию о текущем авторизованном пользователе (его профиль, роли и другие данные)
    @GetMapping("/me")
    public String me(@RequestBody @Valid UserDTO userDTO) {

        return null;
    }

    // Возвращает информацию о конкретном пользователе на основе его идентификатора.
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long userId) {
        User user = usersService.findUser(userId);
        return ResponseEntity.ok(userMapper.userToUserDTO(user));
    }

    // Обновляет информацию о пользователе на основе предоставленного идентификатора.
    @PutMapping("/{userId}")
    public ResponseEntity<HttpStatus> updateUser(@PathVariable Long userId, @RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = ErrorsUtil.getErrorMessage(bindingResult);
            throw new ResourceNotUpdatedException(errorMsg);
        }

        usersService.updateUser(userId, userDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    // Удаляет учетную запись пользователя на основе его идентификатора
    @DeleteMapping("/{userId}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long userId) {
        usersService.deleteUser(userId);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}



