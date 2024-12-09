package com.kimparksin.meatplus.controller;

import com.kimparksin.meatplus.dto.ImageDto;
import com.kimparksin.meatplus.dto.ProjectDto;
import com.kimparksin.meatplus.service.ImageProcessingService;
import com.kimparksin.meatplus.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final ImageProcessingService imageProcessingService;

    public ProjectController(ProjectService projectService, ImageProcessingService imageProcessingService) {
        this.projectService = projectService;
        this.imageProcessingService = imageProcessingService;
    }

    // 프로젝트 추가
    @PostMapping
    public String createProject(@RequestParam("projectName") String projectName,
                                Authentication authentication) {
        String userEmail = authentication.getName(); // 로그인된 사용자 이메일 가져오기
        ProjectDto newProject = new ProjectDto();
        newProject.setProjectName(projectName);

        // 서비스 호출로 프로젝트 생성
        projectService.createProject(newProject, userEmail);

        return "redirect:/users/mypage"; // 프로젝트 추가 후 마이페이지로 리다이렉트
    }

    //모든 프로젝트의 정보를 JSON 배열로 반환
    @GetMapping
    public ResponseEntity<List<ProjectDto>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    //경로 변수 id를 사용하여 특정 프로젝트를 조회
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @GetMapping("/{projectCode}/images")
    public String showImagesByProjectCode(@PathVariable Long projectCode, Model model) {
        // 특정 프로젝트의 이미지 리스트를 가져옵니다.
        List<ImageDto> images = imageProcessingService.getImagesByProjectCode(projectCode);

        // 프로젝트 이름 가져오기
        ProjectDto project = projectService.getProjectByCode(projectCode);

        // View에 필요한 데이터 추가
        model.addAttribute("projectName", project.getProjectName());
        model.addAttribute("images", images);

        return "project_images"; // project_images.html로 이동
    }

}
