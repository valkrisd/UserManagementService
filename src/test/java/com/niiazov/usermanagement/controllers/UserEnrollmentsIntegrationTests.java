package com.niiazov.usermanagement.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.niiazov.usermanagement.UserManagementApplication;
import com.niiazov.usermanagement.dto.EnrollmentDTO;
import com.niiazov.usermanagement.entities.User;
import com.niiazov.usermanagement.repositories.UserRepository;
import com.niiazov.usermanagement.services.KafkaNotificationsService;
import com.niiazov.usermanagement.util.TestEntitiesBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UserManagementApplication.class})
@WebAppConfiguration
@WireMockTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "course_management.url=http://localhost:8080",
        "spring.flyway.enabled=false"}
)
public class UserEnrollmentsIntegrationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private KafkaNotificationsService kafkaNotificationsService;
    @Mock
    private UserRepository userRepository;

    private static EnrollmentDTO enrollmentDTO;
    private static User user;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        enrollmentDTO = TestEntitiesBuilder.buildEnrollmentDTO();
        user = TestEntitiesBuilder.buildUser();
    }

    @Test
    public void createEnrollment_returnsOk() throws Exception {
        doNothing().when(kafkaNotificationsService).sendEnrollmentNotification(enrollmentDTO);
        when(userRepository.findById(enrollmentDTO.getUserId())).thenReturn(Optional.of(user));

        stubFor(WireMock.post(urlEqualTo("/enrollments"))
                .withHeader("Content-Type", equalTo("application/json"))
                .willReturn(created()));

        mockMvc.perform(post("/users/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enrollmentDTO)))
                .andExpect(status().isOk());

        Mockito.verify(kafkaNotificationsService).sendEnrollmentNotification(enrollmentDTO);
        Mockito.verify(userRepository).findById(enrollmentDTO.getUserId());
    }
}
