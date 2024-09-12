package com.niiazov.usermanagement.mappers;

import com.niiazov.usermanagement.dto.UserActivationTokenDTO;
import com.niiazov.usermanagement.entities.UserActivationToken;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserActivationTokenMapper {

    UserActivationTokenDTO toDTO(UserActivationToken userActivationToken);

    UserActivationToken fromDTO(UserActivationTokenDTO userActivationTokenDTO);
}
