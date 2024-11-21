package com.niiazov.usermanagement.services;

import com.niiazov.usermanagement.dto.EnrollmentDTO;
import com.niiazov.usermanagement.entities.User;
import com.niiazov.usermanagement.exceptions.EnrollmentAlreadyExistsException;
import com.niiazov.usermanagement.exceptions.TooManyEnrollmentsException;
import com.niiazov.usermanagement.gateways.CourseManagementGateway;
import com.niiazov.usermanagement.repositories.UserRepository;
import com.niiazov.usermanagement.util.TestEntitiesBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureJsonTesters
@ActiveProfiles("test")
@Testcontainers
@DirtiesContext
public class UserEnrollmentsServiceIntegrationTests {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:13");
    @MockBean
    private CourseManagementGateway courseManagementGateway;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private UserEnrollmentsService userEnrollmentsService;
    @Autowired
    private JacksonTester<EnrollmentDTO> jsonEnrollmentDTO;
    @Autowired
    private JacksonTester<Set<EnrollmentDTO>> jsonEnrollmentDTOSet;
    private EnrollmentDTO enrollmentDTO;
    private Set<EnrollmentDTO> enrollmentDTOs;
    private User user;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeEach
    void setUp() throws IOException {
        enrollmentDTO = jsonEnrollmentDTO.readObject(new ClassPathResource("jsons/enrollmentDTO.json"));
        user = TestEntitiesBuilder.buildUser();
        userRepository.save(user);

    }

    @Test
    void createEnrollment_ok() throws IOException {
        enrollmentDTOs = jsonEnrollmentDTOSet.readObject(new ClassPathResource("jsons/OKEnrollmentDTOs.json"));

        Integer userId = enrollmentDTO.getUserId();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(courseManagementGateway.getEnrollmentsByUserId(userId)).thenReturn(enrollmentDTOs);

        assertDoesNotThrow(() -> userEnrollmentsService.createEnrollment(enrollmentDTO));
        verify(courseManagementGateway).createEnrollment(enrollmentDTO);

    }

    @Test
    void createEnrollment_throwsEnrollmentAlreadyExistsException() throws IOException {
        enrollmentDTOs = jsonEnrollmentDTOSet.readObject(new ClassPathResource("jsons/throwsEnrollmentAlreadyExistsException.json"));

        Integer userId = enrollmentDTO.getUserId();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(courseManagementGateway.getEnrollmentsByUserId(userId)).thenReturn(enrollmentDTOs);

        assertThrows(EnrollmentAlreadyExistsException.class, () -> userEnrollmentsService.createEnrollment(enrollmentDTO));
        verify(courseManagementGateway, never()).createEnrollment(any());
    }

    @Test
    void createEnrollment_throwsTooManyEnrollmentsException() throws IOException {
        enrollmentDTOs = jsonEnrollmentDTOSet.readObject(new ClassPathResource("jsons/throwsTooManyEnrollmentsException.json"));

        Integer userId = enrollmentDTO.getUserId();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(courseManagementGateway.getEnrollmentsByUserId(userId)).thenReturn(enrollmentDTOs);

        assertThrows(TooManyEnrollmentsException.class, () -> userEnrollmentsService.createEnrollment(enrollmentDTO));
        verify(courseManagementGateway, never()).createEnrollment(any());
    }
}
