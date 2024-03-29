package com.netcracker.ncfallprojectrepo.controllers;

import com.netcracker.ncfallprojectrepo.exceptions.ResourceIsAlreadyExistsException;
import com.netcracker.ncfallprojectrepo.exceptions.ResourceNotFoundException;
import com.netcracker.ncfallprojectrepo.module.Project;
import com.netcracker.ncfallprojectrepo.module.Tag;
import com.netcracker.ncfallprojectrepo.module.User;
import com.netcracker.ncfallprojectrepo.module.requested.TagListRequestDto;
import com.netcracker.ncfallprojectrepo.module.requested.TagWithIdListRequestDto;
import com.netcracker.ncfallprojectrepo.module.requested.TagWithIdRequestDto;
import com.netcracker.ncfallprojectrepo.module.requested.TagRequestDto;
import com.netcracker.ncfallprojectrepo.module.responses.TagResponseDto;
import com.netcracker.ncfallprojectrepo.services.ProjectService;
import com.netcracker.ncfallprojectrepo.services.TagService;
import com.netcracker.ncfallprojectrepo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "${cors.urls}")
@RestController
@RequestMapping("/api/tag")
public class TagController {

    @Autowired
    UserService userService;

    @Autowired
    ProjectService projectService;

    @Autowired
    TagService tagService;

    @GetMapping("/get/categories")
    public ResponseEntity<?> getCategories() {

        List<Tag> tagList = tagService.findAllTagByCategoryIsNull();

        List<TagResponseDto> tagResponseDtoList = getTagResponseDtos(tagList);

        return ResponseEntity.ok(tagResponseDtoList);

    }

    @PostMapping("/check/tags")
    public ResponseEntity<?> getCheckedTags(@Valid @RequestBody TagListRequestDto tagListRequestDto) {

        List<Tag> tagList = tagService.findAllTagByCategoryIsNull();

        for (TagRequestDto tagRequestDto : tagListRequestDto.getTags()) {
            tagList.remove(tagService.findTagByName(tagRequestDto.getTagName())
                    .orElseThrow(() -> new ResourceNotFoundException("Error: Tag Not Found")));
            tagList.addAll(tagService.findAllByCategoryNameContains(tagRequestDto.getTagName()));
        }

        List<TagResponseDto> tagResponseDtoList = getTagResponseDtos(tagList);

        return ResponseEntity.ok(tagResponseDtoList);

    }

    @GetMapping("/get/profile")
    public ResponseEntity<?> getUserTags() {

        List<Tag> tagList = tagService.findAllTagByUser(userService.findUserByUsername(getCurrentUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Error: User Not Found")));

        List<TagResponseDto> tagResponseDtoList = getTagResponseDtos(tagList);

        return ResponseEntity.ok(tagResponseDtoList);

    }

    @PostMapping("/add/tag")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> addNewTag(@Valid @RequestBody TagRequestDto tagRequestDto) {

        if (tagService.existsTagByName(tagRequestDto.getTagName()))
            throw new ResourceIsAlreadyExistsException("Error: Tag exists!");

        Tag tag = new Tag();
        tag.setName(tagRequestDto.getTagName());

        if (tagRequestDto.getCategoryName() != null && tagService.existsTagByName(tagRequestDto.getCategoryName()))
            tag.setCategory(tagService.findTagByName(tagRequestDto.getCategoryName())
                    .orElseThrow(() -> new ResourceNotFoundException("Error: Tag Not Found")));

        return ResponseEntity.ok("Added Tag: " + tagService.saveTag(tag).getName());
    }

    @PostMapping("/add/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> addUserToTag(@Valid @RequestBody TagRequestDto tagRequestDto) {

        Set<User> users;
        Tag tag = tagService.findTagByName(tagRequestDto.getTagName())
                .orElseThrow(() -> new ResourceNotFoundException("Error: Tag Not Found"));

        if (tag.getUserList() == null || tag.getUserList().isEmpty())
            users = new HashSet<>();
        else
            users = tag.getUserList();

        users.add(userService.findUserByUsername(getCurrentUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Error: User Not Found")));
        tag.setUserList(users);
        Tag tag2 = tagService.saveTag(tag);

        return ResponseEntity.ok("Added Tag: " + tag2.getName() + " to User: " + getCurrentUsername());
    }

    @PostMapping("/add/project")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> addProjectToTag(@Valid @RequestBody TagWithIdRequestDto tagWithIdRequestDto) {

        Set<Project> projects;
        Tag tag = tagService.findTagByName(tagWithIdRequestDto.getTagName())
                .orElseThrow(() -> new ResourceNotFoundException("Error: Tag Not Found"));

        if (tag.getProjectList() == null || tag.getProjectList().isEmpty())
            projects = new HashSet<>();
        else
            projects = tag.getProjectList();

        Project project = projectService.findProjectById(tagWithIdRequestDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Error: Project Not Found"));
        projects.add(projectService.findProjectById(tagWithIdRequestDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Error: Project Not Found")));
        tag.setProjectList(projects);
        Tag tag2 = tagService.saveTag(tag);

        return ResponseEntity.ok("Added Tag: " + tag2.getName() + " to Project: " + project.getName());
    }

    @PostMapping("/add/project/all")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> addProjectToTag(@Valid @RequestBody TagWithIdListRequestDto tagWithIdListRequestDto) {

        for(TagWithIdRequestDto tagWithIdRequestDto : tagWithIdListRequestDto.getTags())
            addProjectToTag(tagWithIdRequestDto);

        return ResponseEntity.ok("All Tags were added");
    }

    private String getCurrentUsername() {
        return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }

    private List<TagResponseDto> getTagResponseDtos(List<Tag> tagList) {
        List<TagResponseDto> tagResponseDtoList = new ArrayList<>();

        if (tagList.isEmpty()) throw new ResourceNotFoundException("Error: Tags Not Found");

        for (Tag tag : tagList) tagResponseDtoList.add(new TagResponseDto(tag.getName()));
        return tagResponseDtoList;
    }

}
