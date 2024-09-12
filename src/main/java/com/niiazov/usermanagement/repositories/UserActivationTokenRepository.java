package com.niiazov.usermanagement.repositories;

import com.niiazov.usermanagement.entities.UserActivationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserActivationTokenRepository extends JpaRepository<UserActivationToken, Integer> {
    Optional<UserActivationToken> findByToken(String token);
}
