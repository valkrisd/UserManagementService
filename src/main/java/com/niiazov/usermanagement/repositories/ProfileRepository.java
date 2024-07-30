package com.niiazov.usermanagement.repositories;

import com.niiazov.usermanagement.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer> {

    Optional<Profile> findByUser_Id(Integer userId);
}
