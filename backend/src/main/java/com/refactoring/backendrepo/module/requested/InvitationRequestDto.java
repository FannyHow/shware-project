package com.netcracker.ncfallprojectrepo.module.requested;

import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class InvitationRequestDto {

    @NotNull
    private Long projectId;

    @NotNull
    private Long userProjectId;

    private Boolean isAccepted;

    private String username;
}
