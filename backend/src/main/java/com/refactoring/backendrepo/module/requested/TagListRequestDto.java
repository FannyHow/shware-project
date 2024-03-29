package com.netcracker.ncfallprojectrepo.module.requested;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagListRequestDto {

    private List<TagRequestDto> tags;
}
