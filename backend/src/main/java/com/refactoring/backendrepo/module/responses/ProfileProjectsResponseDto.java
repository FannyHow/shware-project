package com.netcracker.ncfallprojectrepo.module.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileProjectsResponseDto {

    private ProjectListResponseDto projectListResponseDto;

    private HashMap<Long, String> connections;

}
