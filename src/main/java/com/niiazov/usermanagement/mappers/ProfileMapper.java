package com.niiazov.usermanagement.mappers;

import com.niiazov.usermanagement.dto.ProfileDTO;
import com.niiazov.usermanagement.models.Profile;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface ProfileMapper {
    ProfileDTO profileToProfileDTO(Profile entity);


    Profile profileDTOToProfile(ProfileDTO dto);
}
