package com.netcracker.ncfallprojectrepo.module.requested;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TagRequestDto {
    @NotBlank
    private String tagName;

    private String categoryName;
}
