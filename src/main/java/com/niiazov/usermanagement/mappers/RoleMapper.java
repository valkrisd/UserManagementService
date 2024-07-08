package com.niiazov.usermanagement.mappers;

import com.niiazov.usermanagement.dto.RoleDTO;
import com.niiazov.usermanagement.models.Role;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface RoleMapper {

    RoleDTO roleToRoleDTO(Role role);

    Role roleDTOToRole(RoleDTO roleDTO);
}
