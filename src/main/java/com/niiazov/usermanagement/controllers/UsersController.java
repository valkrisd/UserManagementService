package com.niiazov.usermanagement.controllers;

import com.niiazov.usermanagement.dto.UserDTO;
import com.niiazov.usermanagement.mappers.UserMapper;
import com.niiazov.usermanagement.models.User;
import com.niiazov.usermanagement.services.UsersService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UsersController {
    private final UsersService usersService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<HttpStatus> register(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        User user = userMapper.userDTOToUser(userDTO);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
        }
        usersService.saveUser(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    // TODO: Implement API with Spring Security later
    @PostMapping("/authenticate")
    public String authenticate(@RequestBody @Valid UserDTO userDTO) {
        User user = userMapper.userDTOToUser(userDTO);

        return null;
    }

    // TODO: Implement API with Spring Security later
    @GetMapping("/me")
    public String me(@RequestBody @Valid UserDTO userDTO) {
        User user = userMapper.userDTOToUser(userDTO);

        return null;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long userId) {
        User user = usersService.findUser(userId);

        if (user != null) return ResponseEntity.ok(userMapper.userToUserDTO(user));
        else return ResponseEntity.notFound().build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<HttpStatus> updateUser(@PathVariable Long userId, @RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        User user = userMapper.userDTOToUser(userDTO);

        if (bindingResult.hasErrors()) return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);

        usersService.updateUser(userId, user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long userId) {
        usersService.deleteUser(userId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}



