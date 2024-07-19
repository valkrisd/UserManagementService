package com.niiazov.usermanagement.gateways;

import com.niiazov.usermanagement.dto.CourseDTO;
import com.niiazov.usermanagement.dto.EnrollmentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "course-management", url = "${course-management.url}")
public interface CourseManagementClient {
    @GetMapping("/users/{userId}/courses")
    List<CourseDTO> getCoursesByUserId(@PathVariable("userId") Long userId);

    @PostMapping("/users/{userId}/enrollments")
    EnrollmentDTO enrollUserToCourse(@PathVariable("userId") Long userId, @RequestBody CourseDTO courseDTO);

    @DeleteMapping("/users/{userId}/enrollments/{enrollmentId}")
    void unenrollUserFromCourse(@PathVariable("userId") Long userId, @PathVariable("enrollmentId") Long enrollmentId);
}
