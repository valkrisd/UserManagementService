package com.niiazov.usermanagement.mappers;

import org.mapstruct.Mapper;

import com.niiazov.usermanagement.dto.UserActivationTokenDTO;
import com.niiazov.usermanagement.models.UserActivationToken;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface UserActivationTokenMapper {

    UserActivationTokenDTO toDTO(UserActivationToken userActivationToken);

    UserActivationToken fromDTO(UserActivationTokenDTO userActivationTokenDTO);
}
