package com.netcracker.ncfallprojectrepo.module.requested;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RefreshTokenRequestDto {

    @NotBlank
    private String refreshToken;

}
