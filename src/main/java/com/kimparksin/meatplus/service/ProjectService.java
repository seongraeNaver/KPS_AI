package com.kimparksin.meatplus.service;

import com.kimparksin.meatplus.dto.ProjectDto;
import com.kimparksin.meatplus.entity.Project;
import com.kimparksin.meatplus.entity.User;
import com.kimparksin.meatplus.repository.ProjectRepository;
import com.kimparksin.meatplus.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    //클라이언트로부터 전달받은 프로젝트 데이터를 데이터베이스에 저장
    //프로젝트를 생성하기 전에, 해당 프로젝트가 연결된 **사용자(User)**를 확인하고 연관 관계를 설정
    public ProjectDto createProject(ProjectDto projectDto, String userEmail) {
        log.info("Creating project for user with email: {}", userEmail);

        // 사용자 이메일로 User 엔티티 조회
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

        // ProjectDto -> Project 변환
        Project project = projectDto.toEntity();
        project.setUser(user); // 사용자와 연관 설정
        Project savedProject = projectRepository.save(project);

        return new ProjectDto(savedProject); // 저장된 프로젝트를 DTO로 반환
    }

    //데이터베이스에서 모든 프로젝트를 조회
    public List<ProjectDto> getAllProjects() {
        log.info("Fetching all projects");
        return projectRepository.findAll()
                .stream()
                .map(ProjectDto::new)
                .collect(Collectors.toList());
    }

    //프로젝트 ID를 기반으로 특정 프로젝트를 조회
    public ProjectDto getProjectById(Long id) {
        log.info("Fetching project with id: {}", id);
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));
        return new ProjectDto(project);
    }

    public ProjectDto getProjectByCode(Long projectCode) {
        Project project = projectRepository.findByProjectCode(projectCode)
                .orElseThrow(() -> new RuntimeException("Project not found with code: " + projectCode));
        return new ProjectDto(project);
    }

    // 특정 사용자의 이메일 기반으로 프로젝트 조회
    public List<ProjectDto> getProjectsByUserEmail(String userEmail) {
        log.info("Fetching projects for user: {}", userEmail);

        // 사용자 이메일로 사용자 엔티티 조회
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

        // 사용자와 연관된 프로젝트 조회
        List<Project> projects = projectRepository.findByUserId(user.getId());

        // Project -> ProjectDto 변환
        return projects.stream()
                .map(ProjectDto::new)
                .collect(Collectors.toList());
    }


}
