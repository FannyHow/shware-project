package com.netcracker.ncfallprojectrepo.module.responses;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckUserAsLeaderResponseDto {

    private Long userProjectId;

    private ProjectResponseDto projectResponseDto;

    private Long userId;

    private String username;

    private String userDescription;

    private List<String> userTagNameList;

}
