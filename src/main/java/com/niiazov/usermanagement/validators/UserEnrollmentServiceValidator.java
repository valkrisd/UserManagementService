package com.niiazov.usermanagement.validators;

import com.niiazov.usermanagement.dto.EnrollmentDTO;
import com.niiazov.usermanagement.exceptions.EnrollmentAlreadyExistsException;
import com.niiazov.usermanagement.exceptions.ResourceNotFoundException;
import com.niiazov.usermanagement.exceptions.TooManyEnrollmentsException;
import com.niiazov.usermanagement.gateways.CourseManagementGateway;
import com.niiazov.usermanagement.repositories.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

@Component
public class UserEnrollmentServiceValidator {
    private static final int MAX_ENROLLMENTS_PER_USER = 3;

    private final UserRepository userRepository;
    private final CourseManagementGateway courseManagementGateway;

    public UserEnrollmentServiceValidator(UserRepository userRepository, CourseManagementGateway courseManagementGateway) {
        this.userRepository = userRepository;
        this.courseManagementGateway = courseManagementGateway;
    }

    public void validate(EnrollmentDTO enrollmentDTO) {
        Integer userId = enrollmentDTO.getUserId();
        Integer courseId = enrollmentDTO.getCourseDTO().getId();
        Set<EnrollmentDTO> enrollments = courseManagementGateway.getEnrollmentsByUserId(userId);

        validateUserExists(userId);

        validateEnrollmentLimits(userId, enrollments);
        validateNotAlreadyEnrolled(userId, courseId, enrollments);
    }

    private void validateUserExists(Integer userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
    }

    private void validateEnrollmentLimits(Integer userId, Set<EnrollmentDTO> enrollments) {

        if (enrollments.size() >= MAX_ENROLLMENTS_PER_USER) {
            throw new TooManyEnrollmentsException("User with id " + userId +
                    " already enrolled in maximum of " + MAX_ENROLLMENTS_PER_USER + " courses");
        }
    }

    private void validateNotAlreadyEnrolled(Integer userId, Integer courseId, Set<EnrollmentDTO> enrollments) {

        boolean isAlreadyEnrolled = enrollments.stream()
                .anyMatch(enrollment -> Objects.equals(enrollment.getCourseDTO().getId(), courseId));

        if (isAlreadyEnrolled) {
            throw new EnrollmentAlreadyExistsException("User with id " + userId +
                    " already enrolled in course with id " + courseId);
        }
    }

}
