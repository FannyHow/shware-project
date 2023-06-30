package com.netcracker.ncfallprojectrepo.services.impl;

import com.netcracker.ncfallprojectrepo.exceptions.ResourceNotFoundException;
import com.netcracker.ncfallprojectrepo.module.Project;
import com.netcracker.ncfallprojectrepo.module.Tag;
import com.netcracker.ncfallprojectrepo.module.User;
import com.netcracker.ncfallprojectrepo.repo.ProjectRepository;
import com.netcracker.ncfallprojectrepo.repo.TagRepository;
import com.netcracker.ncfallprojectrepo.repo.UserRepository;
import com.netcracker.ncfallprojectrepo.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    TagRepository tagRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Override
    public List<Tag> findAllTagByProject(Project project) {
        return tagRepository.findByProjectList_Id(project.getId());
    }

    @Override
    public List<Tag> findAllTagByUser(User user) {
        return tagRepository.findByUserList_Id(user.getId());
    }

    @Override
    public List<Tag> findAllTagByCategoryIsNull() {
        return tagRepository.findByCategoryIsNull();
    }

    @Override
    public List<Tag> findAllByCategoryNameContains(String name) {
        return tagRepository.findByCategoryNameContains(name);
    }

    @Override
    public Optional<Tag> findTagById(Long id) {
        return tagRepository.findById(id);
    }

    @Override
    public Optional<Tag> findTagByName(String name) {
        return tagRepository.findByName(name);
    }

    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public boolean existsTagByName(String name) {
        return tagRepository.existsByName(name);
    }

    @Override
    public void deleteTagFromUsername(String tagName, String username) {

    }

    @Override
    public void deleteTagFromProject(String tagName, Long id) {

    }
}
