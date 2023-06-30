package com.netcracker.ncfallprojectrepo.services;

import com.netcracker.ncfallprojectrepo.module.Project;
import com.netcracker.ncfallprojectrepo.module.Tag;
import com.netcracker.ncfallprojectrepo.module.User;

import java.util.List;
import java.util.Optional;

public interface TagService {

    List<Tag> findAllTagByProject(Project project);

    List<Tag> findAllTagByUser(User user);

    List<Tag> findAllTagByCategoryIsNull();

    List<Tag> findAllByCategoryNameContains(String name);

    Optional<Tag> findTagById(Long id);

    Optional<Tag> findTagByName(String name);

    Tag saveTag(Tag tag);

    boolean existsTagByName(String name);

    void deleteTagFromUsername(String tagName, String username);

    void deleteTagFromProject(String tagName, Long id);

}
