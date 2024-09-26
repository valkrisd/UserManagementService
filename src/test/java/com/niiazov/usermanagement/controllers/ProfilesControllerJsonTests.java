package com.niiazov.usermanagement.controllers;

import com.niiazov.usermanagement.dto.ProfileDTO;
import com.niiazov.usermanagement.util.TestEntitiesBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;
import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ProfilesControllerJsonTests {

    @Autowired
    JacksonTester<ProfileDTO> profileDTOJacksonTester;

    private ProfileDTO profileDTO;

    @BeforeEach
    public void setup() {
        profileDTO = TestEntitiesBuilder.buildProfileDTO();
    }

    @Test
    void testSerializationToJsonFile() throws Exception {
        JsonContent<ProfileDTO> serializedObject = profileDTOJacksonTester.write(profileDTO);
        assertThat(serializedObject).isEqualToJson(new ClassPathResource("jsons/profileDTO.json"));

        assertThat(serializedObject).hasJsonPath("$.fullName");

    }

    @Test
    void testDeserializationOfJsonFile() throws Exception {
        ObjectContent<ProfileDTO> deserializedObject = profileDTOJacksonTester.read(new ClassPathResource("jsons/profileDTO.json"));
        assertThat(deserializedObject.getObject()).isEqualTo(profileDTO);

        assertThat(deserializedObject.getObject().getFullName()).isEqualTo(profileDTO.getFullName());

    }
}
