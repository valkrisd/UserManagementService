package com.niiazov.usermanagement.services;

import com.niiazov.usermanagement.dto.CourseDTO;
import com.niiazov.usermanagement.dto.EnrollmentDTO;
import com.niiazov.usermanagement.exceptions.ResourceNotFoundException;
import com.niiazov.usermanagement.gateways.CourseManagementGateway;
import com.niiazov.usermanagement.entities.User;
import com.niiazov.usermanagement.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserEnrollmentsService {

    private final CourseManagementGateway courseManagementGateway;
    private final UserRepository userRepository;

    public Set<CourseDTO> getUserCourses(Integer userId) {

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) throw new ResourceNotFoundException("User with id " + userId + " not found");

        Set<EnrollmentDTO> enrollments = courseManagementGateway.getEnrollmentsByUserId(userId);

        return enrollments.stream()
                .map(EnrollmentDTO::getCourseDTO)
                .collect(Collectors.toSet());
    }

    public void createEnrollment(EnrollmentDTO enrollmentDTO) {

        Optional<User> user = userRepository.findById(enrollmentDTO.getUserId());
        if (user.isEmpty())
            throw new ResourceNotFoundException("User with id " + enrollmentDTO.getUserId() + " not found");

        courseManagementGateway.createEnrollment(enrollmentDTO);
    }

    public void deleteEnrollment(Integer userId, Integer enrollmentId) {

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) throw new ResourceNotFoundException("User with id " + userId + " not found");

        courseManagementGateway.deleteEnrollment(enrollmentId, userId);
    }
}