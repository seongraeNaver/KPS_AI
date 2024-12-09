package com.kimparksin.meatplus.dto;

import com.kimparksin.meatplus.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {
    private Long projectCode;
    private String projectName;
    private LocalDateTime projectDate;
    private Long userId;

    public ProjectDto(Project project) {
        this.projectCode = project.getProjectCode();
        this.projectName = project.getProjectName();
        this.projectDate = project.getProjectDate();
        this.userId = project.getUser().getId();
    }

    public Project toEntity() {
        Project project = new Project();
        project.setProjectCode(this.projectCode);
        project.setProjectName(this.projectName);
        project.setProjectDate(this.projectDate);

        // User 엔티티는 별도로 조회/설정 필요
        return project;
    }
}
