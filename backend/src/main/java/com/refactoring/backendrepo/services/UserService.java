package com.netcracker.ncfallprojectrepo.services;

import com.netcracker.ncfallprojectrepo.module.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findUserByUsername(String username);

    User saveUser(User user);

}
