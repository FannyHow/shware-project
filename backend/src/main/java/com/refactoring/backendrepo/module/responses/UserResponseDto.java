package com.netcracker.ncfallprojectrepo.module.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    private Long id;
    private String username;
    private String name;
    private String surname;
    private LocalDateTime birthDate;
    private String description;
    private byte[] image;
}
