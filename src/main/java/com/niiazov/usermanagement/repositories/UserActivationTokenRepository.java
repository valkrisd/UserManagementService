package com.niiazov.usermanagement.repositories;

import com.niiazov.usermanagement.models.UserActivationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserActivationTokenRepository extends JpaRepository<UserActivationToken, Integer> {
}
