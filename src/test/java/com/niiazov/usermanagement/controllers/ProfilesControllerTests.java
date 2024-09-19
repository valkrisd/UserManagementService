package com.niiazov.usermanagement.controllers;

import com.niiazov.usermanagement.dto.ProfileDTO;
import com.niiazov.usermanagement.services.ProfilesService;
import com.niiazov.usermanagement.util.TestEntitiesBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Random;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ProfilesControllerTests {

    @Mock
    private ProfilesService profilesService;

    @InjectMocks
    private ProfilesController profilesController;

    private static Integer userId;
    private static ProfileDTO profileDTO;

    @BeforeEach
    public void setup() {
        profileDTO = TestEntitiesBuilder.buildProfileDTO();
        userId = new Random().nextInt();
    }

    @AfterEach
    public void tearDown() {
        Mockito.reset(profilesService);
    }

    @Test
    public void getProfile_returnsProfileDTO() {
        when(profilesService.findProfileDTO(userId)).thenReturn(profileDTO);

        ResponseEntity<ProfileDTO> responseEntity = profilesController.getProfile(userId);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getBody()).isEqualTo(profileDTO);
        Mockito.verify(profilesService).findProfileDTO(userId);
    }

    @Test
    public void updateProfile_returnsOk() {
        doNothing().when(profilesService).updateProfile(userId, profileDTO);

        ResponseEntity<HttpStatus> responseEntity = profilesController.updateProfile(userId, profileDTO);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Mockito.verify(profilesService).updateProfile(userId, profileDTO);
    }
}
