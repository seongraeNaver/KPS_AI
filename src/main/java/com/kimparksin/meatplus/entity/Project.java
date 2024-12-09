package com.kimparksin.meatplus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "project")
@Getter
@Setter
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_code")
    private Long projectCode;

    @Column(name = "project_name", nullable = false, length = 45)
    private String projectName;

    @Column(name = "project_date", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime projectDate = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    // 프로젝트가 저장되기 전에 기본값 설정
    @PrePersist
    public void prePersist() {
        this.projectDate = this.projectDate == null ? LocalDateTime.now() : this.projectDate;
    }
}
