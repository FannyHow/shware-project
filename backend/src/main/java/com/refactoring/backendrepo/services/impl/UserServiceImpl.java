package com.netcracker.ncfallprojectrepo.services.impl;

import com.netcracker.ncfallprojectrepo.exceptions.ResourceNotFoundException;
import com.netcracker.ncfallprojectrepo.module.User;
import com.netcracker.ncfallprojectrepo.repo.UserRepository;
import com.netcracker.ncfallprojectrepo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

}
