package com.niiazov.usermanagement.repositories;

import com.niiazov.usermanagement.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    List<Role> findByNameIn(Set<String> roleNames);
}
