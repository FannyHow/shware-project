package com.netcracker.ncfallprojectrepo.module;

import java.security.Principal;

public class SocketUser implements Principal {

    private String name;

    public SocketUser(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

}