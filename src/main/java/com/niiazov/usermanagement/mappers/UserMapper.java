package com.niiazov.usermanagement.mappers;

import com.niiazov.usermanagement.dto.UserDTO;
import com.niiazov.usermanagement.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO userToUserDTO(User user);

    User userDTOToUser(UserDTO userDTO);


}
