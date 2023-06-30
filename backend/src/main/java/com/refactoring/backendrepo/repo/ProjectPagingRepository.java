package com.netcracker.ncfallprojectrepo.repo;

import com.netcracker.ncfallprojectrepo.module.Project;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ProjectPagingRepository extends PagingAndSortingRepository<Project, Long> {
    List<Project> findByUserProjectListUserOrderByIdWhereUserProjectListProjectIsFavouriteIsTrue();

    List<Project> findByNameContainingOrderById(String name, Pageable pageable);

    List<Project> findProjectsByTagListNameOrderById(String tagName, Pageable pageable);

    List<Project> findProjectsByNameContainingAndTagListNameOrderById(String name, String tagName, Pageable pageable);

    List<Project> findProjectsByNameContainingAndTagListNameInOrderById(String name, List<String> tagNameList, Pageable pageable);
}
