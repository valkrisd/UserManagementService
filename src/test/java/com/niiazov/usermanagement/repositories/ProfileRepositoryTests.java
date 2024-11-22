package com.niiazov.usermanagement.repositories;

import com.niiazov.usermanagement.entities.Profile;
import com.niiazov.usermanagement.entities.User;
import com.niiazov.usermanagement.util.TestEntitiesBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles("test")
public class ProfileRepositoryTests {

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void testProfileIsPresent() {

        User user = TestEntitiesBuilder.buildUserWithEmptyRolesSet();
        User savedUser = userRepository.save(user);

        Profile profile = TestEntitiesBuilder.buildProfile(savedUser);
        Profile savedProfile = profileRepository.save(profile);

        Optional<Profile> foundProfile = profileRepository.findByUser_Id(savedUser.getId());

        Assertions.assertNotNull(savedProfile);
        Assertions.assertTrue(foundProfile.isPresent());
        Assertions.assertEquals(savedProfile.getId(), foundProfile.get().getId());
    }

}


