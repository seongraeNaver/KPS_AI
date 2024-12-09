package com.kimparksin.meatplus.repository;

import com.kimparksin.meatplus.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByUserId(Long userId); // 특정 사용자와 관련된 프로젝트 모두찾기

    Optional<Project> findByProjectCodeAndUser_Id(Long projectCode, Long userId);

    Optional<Project> findByProjectCode(Long projectCode); // 프로젝트 코드로 프로젝트 조회

}
