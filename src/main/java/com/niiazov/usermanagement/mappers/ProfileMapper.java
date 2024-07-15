package com.niiazov.usermanagement.mappers;

import com.niiazov.usermanagement.dto.ProfileDTO;
import com.niiazov.usermanagement.models.Profile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileDTO profileToProfileDTO(Profile entity);


    Profile profileDTOToProfile(ProfileDTO dto);
}
