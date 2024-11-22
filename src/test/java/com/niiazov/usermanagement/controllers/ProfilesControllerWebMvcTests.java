package com.niiazov.usermanagement.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.niiazov.usermanagement.dto.ProfileDTO;
import com.niiazov.usermanagement.services.ProfilesService;
import com.niiazov.usermanagement.util.TestEntitiesBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Random;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ProfilesController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class ProfilesControllerWebMvcTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProfilesService profilesService;

    private Integer userId;
    private ProfileDTO profileDTO;

    @BeforeEach
    public void setup() {
        profileDTO = TestEntitiesBuilder.buildProfileDTO();
        userId = new Random().nextInt(0, 1000);
    }


    @AfterEach
    public void tearDown() {
        Mockito.reset(profilesService);
    }

    @Test
    public void getProfile_returnsProfileDTO() throws Exception {
        when(profilesService.findProfileDTO(userId)).thenReturn(profileDTO);

        mockMvc.perform(get("/users/{userId}/profiles", userId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(profileDTO)));

        verify(profilesService).findProfileDTO(userId);
    }

    @Test
    public void updateProfile_returnsOk() throws Exception {
        doNothing().when(profilesService).updateProfile(userId, profileDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/users/{userId}/profiles", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileDTO)))
                .andExpect(status().isOk());

        verify(profilesService).updateProfile(userId, profileDTO);
    }
}
