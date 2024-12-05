package com.niiazov.usermanagement.controllers;

import com.niiazov.usermanagement.dto.CourseDTO;
import com.niiazov.usermanagement.dto.EnrollmentDTO;
import com.niiazov.usermanagement.services.KafkaNotificationsService;
import com.niiazov.usermanagement.services.UserEnrollmentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User Enrollments", description = "Operations for managing user course enrollments")
public class UserEnrollmentsController {

    private final UserEnrollmentsService userEnrollmentsService;
    private final KafkaNotificationsService kafkaNotificationsService;

    @Operation(
            summary = "Get user enrollments",
            description = "Fetch all course enrollments for a specific user by their ID.",
            tags = {"User Enrollments"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enrollments retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping("/{userId}/courses")
    public ResponseEntity<Set<CourseDTO>> getEnrollmentsByUser(
            @Parameter(description = "ID of the user to fetch enrollments for", example = "1")
            @PathVariable Integer userId) {
        log.info("Request to get enrollments for user id: {}", userId);
        Set<CourseDTO> courseDTOS = userEnrollmentsService.getUserCourses(userId);
        log.debug("Enrollments for user id: {} successfully found", userId);

        return ResponseEntity.ok(courseDTOS);
    }

    @Operation(
            summary = "Create a new enrollment",
            description = "Enroll a user into a course.",
            tags = {"User Enrollments"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enrollment created successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid enrollment data", content = @Content)
    })
    @PostMapping("/enrollments")
    public ResponseEntity<HttpStatus> createEnrollment(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details of the enrollment to create",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EnrollmentDTO.class)),
                    required = true
            )
            @RequestBody @Valid EnrollmentDTO enrollmentDTO) {
        log.info("Try to create enrollment for user with ID: {}", enrollmentDTO.getUserId());
        userEnrollmentsService.createEnrollment(enrollmentDTO);
        log.info("Enrollment for user with ID: {} successfully created", enrollmentDTO.getUserId());

        kafkaNotificationsService.sendEnrollmentNotification(enrollmentDTO);
        log.info("Enrollment notification for user with ID: {} successfully sent", enrollmentDTO.getUserId());

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(
            summary = "Delete an enrollment",
            description = "Remove a specific enrollment for a user by their user ID and enrollment ID.",
            tags = {"User Enrollments"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enrollment deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Enrollment or user not found", content = @Content)
    })
    @DeleteMapping("/{userId}/enrollments/{enrollmentId}")
    public ResponseEntity<HttpStatus> deleteEnrollment(
            @Parameter(description = "ID of the user whose enrollment is to be deleted", example = "1")
            @PathVariable Integer userId,
            @Parameter(description = "ID of the enrollment to delete", example = "100")
            @PathVariable Integer enrollmentId) {
        log.info("Try to delete enrollment for user with ID: {}", userId);
        userEnrollmentsService.deleteEnrollment(userId, enrollmentId);
        log.info("Enrollment for user with ID: {} successfully deleted", userId);

        return ResponseEntity.ok(HttpStatus.OK);
    }
}

