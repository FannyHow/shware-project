package com.netcracker.ncfallprojectrepo.services.impl;
import com.netcracker.ncfallprojectrepo.module.Project;
import com.netcracker.ncfallprojectrepo.module.User;
import com.netcracker.ncfallprojectrepo.repo.ProjectPagingRepository;
import com.netcracker.ncfallprojectrepo.repo.ProjectRepository;
import com.netcracker.ncfallprojectrepo.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectPagingRepository projectPagingRepository;

    @Override
    public Project saveProject(Project project) {
       return projectRepository.save(project);
    }

    @Override
    public Optional<Project> findProjectById(Long id) {
        return projectRepository.findById(id);
    }

    @Override
    public List<Project> getAllProject() {
        return projectRepository.findAll();
    }

    @Override
    public Long getNumberOfProject() {
        return projectRepository.count();
    }

    @Override
    public List<Project> getAllProjectsByUser(User user) {
        return projectRepository.findByUserProjectListUserOrderById(user);
    }

    @Override
    public List<Project> getAllProjectWithName(String name) {
        return projectRepository.findByNameContainingOrderById(name);
    }

    @Override
    public List<Project> getAllFavouriteProjectByUser(User user) {
        return projectRepository.findByUserProjectListUserOrderByIdWhereUserProjectListProjectIsFavouriteIsTrue(user); //Да, это сработало, сам в шоке, оставлю для истории
    }

    @Override
    public Page<Project> getAllProjectPageable(int page, int size) {
        return projectPagingRepository.findAll(PageRequest.of(page,size));
    }

    @Override
    public List<Project> getAllProjectWithNamePageable(String name, int page, int size) {
        return projectPagingRepository.findByNameContainingOrderById(name, PageRequest.of(page,size));
    }

    @Override
    public List<Project> getAllProjectWithTagPageable(String tagName, int page, int size) {
        return projectPagingRepository.findProjectsByTagListNameOrderById(tagName, PageRequest.of(page,size));
    }

    @Override
    public List<Project> getAllProjectWithTagAndNamePageable(String name, String tagName, int page, int size) {
        return projectPagingRepository.findProjectsByNameContainingAndTagListNameOrderById(name, tagName, PageRequest.of(page,size));
    }

    @Override
    public List<Project> getAllProjectsWithTagsAndNamePageable(String name, List<String> tagNameList, int page, int size) {
        return projectPagingRepository.findProjectsByNameContainingAndTagListNameInOrderById(name, tagNameList, PageRequest.of(page, size));
    }

}

