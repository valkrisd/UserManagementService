package com.niiazov.usermanagement.controllers;

import com.niiazov.usermanagement.dto.UserDTO;
import com.niiazov.usermanagement.exceptions.ResourceNotCreatedException;
import com.niiazov.usermanagement.exceptions.ResourceNotUpdatedException;
import com.niiazov.usermanagement.services.UsersService;
import com.niiazov.usermanagement.util.ErrorsUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {
    private final UsersService usersService;

    // TODO: Add password encryption later
    @PostMapping("/register")
    public ResponseEntity<HttpStatus> register(@RequestBody @Valid UserDTO userDTO,
                                               BindingResult bindingResult) {
        log.info("Попытка регистрации нового пользователя");
        if (bindingResult.hasErrors()) {
            String errorMsg = ErrorsUtil.getErrorMessage(bindingResult);
            log.error("Ошибка валидации при регистрации пользователя: {}", errorMsg);
            throw new ResourceNotCreatedException(errorMsg);
        }

        usersService.saveUser(userDTO);
        log.info("Пользователь успешно зарегистрирован: {}", userDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    // TODO: Implement API with Spring Security later
    @PostMapping("/authenticate")
    public String authenticate(@RequestBody @Valid UserDTO userDTO) {
        log.info("Аутентификация пользователя: {}", userDTO);
        return "Test response";
    }

    // TODO: Implement API with Spring Security later
    @GetMapping("/me")
    public String me(@RequestBody @Valid UserDTO userDTO) {
        log.info("[TEMP] Получение информации о текущем пользователе: {}", userDTO);
        return "Test response";
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Integer userId) {
        log.info("Запрос данных пользователя с id: {}", userId);
        UserDTO userDTO = usersService.getUserDTO(userId);
        log.info("Пользователь с id: {} успешно найден", userId);
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<HttpStatus> updateUser(@PathVariable Integer userId,
                                                 @RequestBody @Valid UserDTO userDTO,
                                                 BindingResult bindingResult) {
        log.info("Обновление данных пользователя с id: {}", userId);
        if (bindingResult.hasErrors()) {
            String errorMsg = ErrorsUtil.getErrorMessage(bindingResult);
            log.error("Ошибка валидации при обновлении пользователя с id: {}: {}", userId, errorMsg);
            throw new ResourceNotUpdatedException(errorMsg);
        }

        usersService.updateUser(userId, userDTO);
        log.info("Данные пользователя с id: {} успешно обновлены", userId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Integer userId) {
        log.info("Удаление пользователя с id: {}", userId);
        usersService.deleteUser(userId);
        log.info("Пользователь с id: {} успешно удален", userId);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}



