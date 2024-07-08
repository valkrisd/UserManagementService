package com.niiazov.usermanagement.services;

import com.niiazov.usermanagement.models.Profile;
import com.niiazov.usermanagement.models.User;
import com.niiazov.usermanagement.repositories.ProfileRepository;
import com.niiazov.usermanagement.util.ProfileNotFoundException;
import com.niiazov.usermanagement.util.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProfilesService {

    private final ProfileRepository profileRepository;
    public Profile findProfile(Long userId) {
        Optional<Profile> optionalProfile = profileRepository.findByUser_Id(userId);
        return optionalProfile.orElse(null);
    }
    @Transactional
    public void updateProfile(Long userId, Profile profile) {
        Optional<Profile> profileToUpdate = profileRepository.findByUser_Id(userId);

        if (profileToUpdate.isEmpty()) {
            throw new ProfileNotFoundException("Profile with user_id " + userId + " not found");
        }
        profileToUpdate.ifPresent(updatedProfile -> {
            updatedProfile.setGender(profile.getGender());
            updatedProfile.setFullName(profile.getFullName());
            updatedProfile.setAddress(profile.getAddress());
            updatedProfile.setDateOfBirth(profile.getDateOfBirth());
            updatedProfile.setPhoneNumber(profile.getPhoneNumber());
            updatedProfile.setAvatarUrl(profile.getAvatarUrl());

            profileRepository.save(updatedProfile);
        });
    }
}
