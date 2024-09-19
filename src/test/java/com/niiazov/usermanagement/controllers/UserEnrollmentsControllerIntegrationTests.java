package com.niiazov.usermanagement.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.niiazov.usermanagement.dto.EnrollmentDTO;
import com.niiazov.usermanagement.entities.User;
import com.niiazov.usermanagement.gateways.CourseManagementGateway;
import com.niiazov.usermanagement.repositories.UserRepository;
import com.niiazov.usermanagement.util.TestEntitiesBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WireMockTest(httpPort = 8081)
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserEnrollmentsControllerIntegrationTests {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13");

    @Container
    private static final KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

    @MockBean
    private CourseManagementGateway courseManagementGateway;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private EnrollmentDTO enrollmentDTO;


    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }

    @BeforeEach
    public void setup() {
        user = TestEntitiesBuilder.buildUserWithEmptyRolesSet();
        userRepository.save(user);

        enrollmentDTO = TestEntitiesBuilder.buildEnrollmentDTO();
    }

    @AfterEach
    public void tearDown() {
        Mockito.reset(courseManagementGateway);
        userRepository.deleteAll();
    }

    @Test
    public void createEnrollment_returnsOk() throws Exception {

        stubFor(WireMock.post(urlEqualTo("/enrollments"))
                .withHeader("Content-Type", equalTo("application/json"))
                .willReturn(created()));

        mockMvc.perform(post("/users/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enrollmentDTO)))
                .andExpect(status().isOk())
                .andReturn();

        Mockito.verify(courseManagementGateway).createEnrollment(enrollmentDTO);
    }
}
