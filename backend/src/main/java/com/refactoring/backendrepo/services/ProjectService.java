package com.netcracker.ncfallprojectrepo.services;

import com.netcracker.ncfallprojectrepo.module.Project;
import com.netcracker.ncfallprojectrepo.module.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProjectService {

    Project saveProject(Project project);

    Optional<Project> findProjectById(Long id);

    List<Project> getAllProject();

    Long getNumberOfProject();

    List<Project> getAllProjectsByUser(User user);

    List<Project> getAllFavouriteProjectByUser(User user);

    List<Project> getAllProjectWithName(String name);

    Page<Project> getAllProjectPageable(int page, int size);

    List<Project> getAllProjectWithNamePageable(String name, int page, int size);

    List<Project> getAllProjectWithTagPageable(String tagName, int page, int size);

    List<Project> getAllProjectWithTagAndNamePageable(String name, String tagName, int page, int size);

    List<Project> getAllProjectsWithTagsAndNamePageable(String name, List<String> tagNameList, int page, int size);
}
