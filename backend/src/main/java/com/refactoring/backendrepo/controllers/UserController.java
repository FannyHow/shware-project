package com.netcracker.ncfallprojectrepo.controllers;

import com.netcracker.ncfallprojectrepo.exceptions.ResourceNotFoundException;
import com.netcracker.ncfallprojectrepo.module.User;
import com.netcracker.ncfallprojectrepo.module.requested.AddUserInfoRequestDto;
import com.netcracker.ncfallprojectrepo.module.responses.UserResponseDto;
import com.netcracker.ncfallprojectrepo.services.ProjectService;
import com.netcracker.ncfallprojectrepo.services.TagService;
import com.netcracker.ncfallprojectrepo.services.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "${cors.urls}")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ProjectService projectService;

    @Autowired
    TagService tagService;

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo() {
        User user = userService.findUserByUsername(getCurrentUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Error: User Not Found"));
        UserResponseDto userResponseDto = getUserResponseDto(user);
        return ResponseEntity.ok(userResponseDto);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUserInfo(@Valid @RequestBody AddUserInfoRequestDto addUserInfoRequestDto) {

        User user = userService.findUserByUsername(getCurrentUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Error: User Not Found"));

        if (!StringUtils.isBlank(addUserInfoRequestDto.getName()))
            user.setName(addUserInfoRequestDto.getName());
        if (!StringUtils.isBlank(addUserInfoRequestDto.getSurname()))
            user.setSurname(addUserInfoRequestDto.getSurname());
        if (!StringUtils.isBlank(addUserInfoRequestDto.getBirthDate().toString()))
            user.setBirth_date(addUserInfoRequestDto.getBirthDate());
        if (!StringUtils.isBlank(addUserInfoRequestDto.getDescription()))
            user.setDescription(addUserInfoRequestDto.getDescription());

        UserResponseDto userResponseDto = getUserResponseDto(userService.saveUser(user));

        return ResponseEntity.ok(userResponseDto);
    }

    private String getCurrentUsername() {
        return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }

    private UserResponseDto getUserResponseDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setDescription(user.getDescription());
        userResponseDto.setUsername(user.getUsername());
        userResponseDto.setImage(user.getImage());
        userResponseDto.setName(user.getName());
        userResponseDto.setBirthDate(user.getBirth_date());
        userResponseDto.setSurname(user.getSurname());
        return userResponseDto;
    }
}
