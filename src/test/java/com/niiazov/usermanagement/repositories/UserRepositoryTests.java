package com.niiazov.usermanagement.repositories;

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
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testUserIsFound() {

        User user = TestEntitiesBuilder.buildUserWithEmptyRolesSet();
        User savedUser = userRepository.save(user);

        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        Assertions.assertTrue(foundUser.isPresent());
        Assertions.assertEquals(savedUser.getId(), foundUser.get().getId());

    }

    @Test
    void testUserIsDeleted() {

        User user = TestEntitiesBuilder.buildUserWithEmptyRolesSet();
        User savedUser = userRepository.save(user);

        userRepository.deleteById(savedUser.getId());
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        Assertions.assertTrue(foundUser.isEmpty());
    }

    @Test
    void testUserIsUpdated() {

        User user = TestEntitiesBuilder.buildUserWithEmptyRolesSet();
        User savedUser = userRepository.save(user);

        User userToUpdate = userRepository.findById(savedUser.getId()).get();

        userToUpdate.setUsername("jane");
        userToUpdate.setEmail("jane@jane");

        userRepository.save(userToUpdate);

        Assertions.assertNotEquals(user.getUsername(), userToUpdate.getUsername());
        Assertions.assertNotEquals(user.getEmail(), userToUpdate.getEmail());

    }

}
