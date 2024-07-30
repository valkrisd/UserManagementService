package com.niiazov.usermanagement.services;

import com.niiazov.usermanagement.dto.ProfileDTO;
import com.niiazov.usermanagement.mappers.ProfileMapper;
import com.niiazov.usermanagement.models.Profile;
import com.niiazov.usermanagement.repositories.ProfileRepository;
import com.niiazov.usermanagement.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfilesService {

    private final ProfileRepository profileRepository;

    private final ProfileMapper profileMapper;

    public ProfileDTO findProfileDTO(Integer userId) {

        return profileRepository.findByUser_Id(userId).map(profileMapper::profileToProfileDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Profile with user_id " + userId + " not found"));
    }

    public Profile findProfile(Integer userId) {

        return profileRepository.findByUser_Id(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile with user_id " + userId + " not found"));
    }

    @Transactional
    public void updateProfile(Integer userId, ProfileDTO profileDTO) {
        Profile profile = profileMapper.profileDTOToProfile(profileDTO);

        Profile profileToUpdate = findProfile(userId);

        profileToUpdate.setGender(profile.getGender());
        profileToUpdate.setFullName(profile.getFullName());
        profileToUpdate.setAddress(profile.getAddress());
        profileToUpdate.setDateOfBirth(profile.getDateOfBirth());
        profileToUpdate.setPhoneNumber(profile.getPhoneNumber());
        profileToUpdate.setAvatarUrl(profile.getAvatarUrl());
    }
}
