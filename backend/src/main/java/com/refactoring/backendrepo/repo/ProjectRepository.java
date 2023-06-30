package com.netcracker.ncfallprojectrepo.repo;

import com.netcracker.ncfallprojectrepo.module.Project;
import com.netcracker.ncfallprojectrepo.module.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findById(Long id);

    List<Project> findByNameContainingOrderById(String name);

    List<Project> findByUserProjectListUserOrderById(User user);

}
