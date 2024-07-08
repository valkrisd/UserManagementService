package com.niiazov.usermanagement.mappers;

import com.niiazov.usermanagement.dto.UserDTO;
import com.niiazov.usermanagement.models.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface UserMapper {

    UserDTO userToUserDTO(User user);

    User userDTOToUser(UserDTO userDTO);



}
