package com.niiazov.usermanagement.controllers;

import com.niiazov.usermanagement.dto.CourseDTO;
import com.niiazov.usermanagement.dto.EnrollmentDTO;
import com.niiazov.usermanagement.services.UserEnrollmentsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserEnrollmentsController {

    private final UserEnrollmentsService userEnrollmentsService;

    @GetMapping("/{userId}/courses")
    public ResponseEntity<Set<CourseDTO>> getEnrollmentsByUser(@PathVariable Integer userId) {
        log.info("Запрос записей для пользователя с id: {}", userId);
        Set<CourseDTO> courseDTOS = userEnrollmentsService.getUserCourses(userId);
        log.debug("Записи для пользователя с id: {} успешно получены", userId);

        return ResponseEntity.ok(courseDTOS);
    }

    @PostMapping("/enrollments")
    public ResponseEntity<HttpStatus> createEnrollment(@RequestBody @Valid EnrollmentDTO enrollmentDTO,
                                                       BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.error("Ошибка валидации при создании записи на курс для пользователя с ID: {}", enrollmentDTO.getUserId());
            return ResponseEntity.badRequest().build();
        }

        log.info("Попытка создания записи на курс для пользователя с ID: {}", enrollmentDTO.getUserId());
        userEnrollmentsService.createEnrollment(enrollmentDTO);
        log.info("Запись на курс для пользователя с ID: {} успешно создана", enrollmentDTO.getUserId());

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/enrollments/{enrollmentId}")
    public ResponseEntity<HttpStatus> deleteEnrollment(@PathVariable Integer userId,
                                                       @PathVariable Integer enrollmentId) {
        log.info("Попытка удаления записи на курс для пользователя с ID: {}", userId);
        userEnrollmentsService.deleteEnrollment(userId, enrollmentId);
        log.info("Запись на курс для пользователя с ID: {} успешно удалена", userId);

        return ResponseEntity.ok(HttpStatus.OK);
    }

}
