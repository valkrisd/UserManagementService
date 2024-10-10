package com.niiazov.usermanagement.mappers;

import com.niiazov.usermanagement.dto.RoleDTO;
import com.niiazov.usermanagement.entities.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleDTO roleToRoleDTO(Role role);

    Role roleDTOToRole(RoleDTO roleDTO);
}
