package com.netcracker.ncfallprojectrepo.repo;

import com.netcracker.ncfallprojectrepo.module.ERole;
import com.netcracker.ncfallprojectrepo.module.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
