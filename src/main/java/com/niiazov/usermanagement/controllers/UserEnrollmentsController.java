package com.niiazov.usermanagement.controllers;

import com.niiazov.usermanagement.dto.CourseDTO;
import com.niiazov.usermanagement.dto.EnrollmentDTO;
import com.niiazov.usermanagement.services.KafkaNotificationsService;
import com.niiazov.usermanagement.services.UserEnrollmentsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserEnrollmentsController {

    private final UserEnrollmentsService userEnrollmentsService;
    private final KafkaNotificationsService kafkaNotificationsService;

    @GetMapping("/{userId}/courses")
    public ResponseEntity<Set<CourseDTO>> getEnrollmentsByUser(@PathVariable Integer userId) {
        log.info("Request to get enrollments for user id: {}", userId);
        Set<CourseDTO> courseDTOS = userEnrollmentsService.getUserCourses(userId);
        log.debug("Enrollments for user id: {} successfully found", userId);

        return ResponseEntity.ok(courseDTOS);
    }

    @PostMapping("/enrollments")
    public ResponseEntity<HttpStatus> createEnrollment(@RequestBody @Valid EnrollmentDTO enrollmentDTO) {

        log.info("Try to create enrollment for user with ID: {}", enrollmentDTO.getUserId());
        userEnrollmentsService.createEnrollment(enrollmentDTO);
        log.info("Enrollment for user with ID: {} successfully created", enrollmentDTO.getUserId());

        kafkaNotificationsService.sendEnrollmentNotification(enrollmentDTO);
        log.info("Enrollment notification for user with ID: {} successfully sent", enrollmentDTO.getUserId());

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/enrollments/{enrollmentId}")
    public ResponseEntity<HttpStatus> deleteEnrollment(@PathVariable Integer userId,
                                                       @PathVariable Integer enrollmentId) {
        log.info("Try to delete enrollment for user with ID: {}", userId);
        userEnrollmentsService.deleteEnrollment(userId, enrollmentId);
        log.info("Enrollment for user with ID: {} successfully deleted", userId);

        return ResponseEntity.ok(HttpStatus.OK);
    }

}
