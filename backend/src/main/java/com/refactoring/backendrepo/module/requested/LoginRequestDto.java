package com.netcracker.ncfallprojectrepo.module.requested;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data

public class LoginRequestDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
