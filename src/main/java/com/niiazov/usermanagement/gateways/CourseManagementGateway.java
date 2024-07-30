package com.niiazov.usermanagement.gateways;

import com.niiazov.usermanagement.dto.EnrollmentDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@FeignClient(name = "course-management", url = "${course-management.url}")
public interface CourseManagementGateway {

    @GetMapping("/enrollments/user/{userId}")
    Set<EnrollmentDTO> getEnrollmentsByUserId(@PathVariable("userId") Integer userId);

    @PostMapping("/enrollments")
    void createEnrollment(@RequestBody @Valid EnrollmentDTO enrollmentDTO);

    @DeleteMapping("/enrollments/{enrollmentId}/user/{userId}")
    void deleteEnrollment(@PathVariable("enrollmentId") Integer enrollmentId, @PathVariable("userId") Integer userId);
}
